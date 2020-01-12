package hr.fer.grupa.erestoran.order

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.PieDialog
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.activity.MethodSelectActivity
import hr.fer.grupa.erestoran.databinding.OrderBaseActivityBinding
import hr.fer.grupa.erestoran.drink.DrinkFragment
import hr.fer.grupa.erestoran.food.FoodFragment
import hr.fer.grupa.erestoran.models.*
import hr.fer.grupa.erestoran.overview.OrderOverviewFragment
import hr.fer.grupa.erestoran.restaurants.RestaurantsFragment
import hr.fer.grupa.erestoran.util.ShakeListener
import hr.fer.grupa.erestoran.util.userUid
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class OrderBaseActivity : AppCompatActivity() {

    private lateinit var binding: OrderBaseActivityBinding

    private lateinit var order: Order

    private var orderType = ""

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private val shakeDetector = ShakeListener()

    private lateinit var pieMenu: PieDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.order_base_activity)
        binding.activity = this
        orderType = intent.getStringExtra("type")!!

        val oldOrder = intent.getSerializableExtra("order") as? Order
        if (oldOrder != null) {
            order = oldOrder
            binding.title.text = "Overview"
            val fragment = OrderOverviewFragment()
            val bundle = Bundle()
            bundle.putSerializable("order", oldOrder)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.container, fragment, "overview")
                .addToBackStack("overview").commit()
        } else {
            binding.title.text = "Pick restaurant"
            val restaurantPickFragment = RestaurantsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, restaurantPickFragment, "restaurant")
                .addToBackStack("restaurant").commit()
        }

        pieMenu = PieDialog(this)
        pieMenu.setCancelable(true)
        pieMenu.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        pieMenu.window?.setBackgroundDrawableResource(android.R.color.transparent)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector.setOnShakeListener(object : ShakeListener.OnShakeListener {

            override fun onShake(count: Int) {
                pieMenu.show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(shakeDetector)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFragmentEvent(event: OrderFragmentEvent) {
        when (event.fragment) {
            is RestaurantsFragment -> {
                binding.title.text = "Pick food"
                order =
                    Order(restaurant = event.data as Restaurant)
                val fragment = FoodFragment()
                val bundle = Bundle()
                bundle.putString("restaurant", order.restaurant.id)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().add(R.id.container, fragment, "food")
                    .addToBackStack("food").setCustomAnimations(
                        R.anim.fragment_enter_animation,
                        R.anim.fragment_stay_animation,
                        R.anim.fragment_stay_animation,
                        R.anim.fragment_exit_animation
                    ).commit()
            }
            is FoodFragment -> {
                binding.title.text = "Pick drinks"
                order.food = (event.data as MutableSet<Food>).toMutableList()
                val fragment = DrinkFragment()
                val bundle = Bundle()
                bundle.putString("restaurant", order.restaurant.id)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().add(R.id.container, fragment, "drink")
                    .addToBackStack("drink").setCustomAnimations(
                        R.anim.fragment_enter_animation,
                        R.anim.fragment_stay_animation,
                        R.anim.fragment_stay_animation,
                        R.anim.fragment_exit_animation
                    ).commit()
            }
            is DrinkFragment -> {
                binding.title.text = "Overview"
                order.drink = (event.data as MutableSet<Drink>).toMutableList()
                val fragment = OrderOverviewFragment()
                val bundle = Bundle()
                bundle.putSerializable("order", order)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().add(R.id.container, fragment, "overview")
                    .addToBackStack("overview").setCustomAnimations(
                        R.anim.fragment_enter_animation,
                        R.anim.fragment_stay_animation,
                        R.anim.fragment_stay_animation,
                        R.anim.fragment_exit_animation
                    ).commit()
            }
        }
    }

    fun homeClicked() {
        startActivity(Intent(this, MethodSelectActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.fragments.last()) {
            is OrderOverviewFragment -> binding.title.text = "Pick drinks"
            is DrinkFragment -> binding.title.text = "Pick food"
            is FoodFragment -> binding.title.text = "Pick restaurant"
        }
        if (supportFragmentManager.fragments.size > 1) supportFragmentManager.popBackStack()
        else {
            startActivity(Intent(this, MethodSelectActivity::class.java))
            finish()
        }
    }
}