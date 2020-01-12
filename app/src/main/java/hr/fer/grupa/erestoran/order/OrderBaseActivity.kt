package hr.fer.grupa.erestoran.order

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.activity.MethodSelectActivity
import hr.fer.grupa.erestoran.activity.UserTypeSelectActivity
import hr.fer.grupa.erestoran.databinding.OrderBaseActivityBinding
import hr.fer.grupa.erestoran.drink.DrinkFragment
import hr.fer.grupa.erestoran.food.FoodFragment
import hr.fer.grupa.erestoran.menu.PieDialog
import hr.fer.grupa.erestoran.models.*
import hr.fer.grupa.erestoran.overview.OrderOverviewFragment
import hr.fer.grupa.erestoran.restaurants.RestaurantsFragment
import hr.fer.grupa.erestoran.util.ShakeListener
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
            orderType = oldOrder.type
            binding.title.text = getString(R.string.overview)
            val fragment = OrderOverviewFragment()
            val bundle = Bundle()
            bundle.putSerializable("order", oldOrder)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.container, fragment, "overview")
                .addToBackStack("overview").commit()
        } else {
            binding.title.text = getString(R.string.pick_restaurant)
            val restaurantPickFragment = RestaurantsFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container, restaurantPickFragment, "restaurant")
                .addToBackStack("restaurant").commit()
            order = Order(type = orderType)
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
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
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
                binding.title.text = getString(R.string.pick_food)
                order.restaurant = event.data as Restaurant
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
                binding.title.text = getString(R.string.pick_drinks)
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
                binding.title.text = getString(R.string.overview)
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
        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        if (prefs.getBoolean("isGuest", false)) {
            startActivity(Intent(this, UserTypeSelectActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MethodSelectActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        when (supportFragmentManager.fragments.last()) {
            is OrderOverviewFragment -> binding.title.text = getString(R.string.pick_drinks)
            is DrinkFragment -> binding.title.text = getString(R.string.pick_food)
            is FoodFragment -> binding.title.text = getString(R.string.pick_restaurant)
        }
        if (supportFragmentManager.fragments.size > 1) supportFragmentManager.popBackStack()
        else {
            if (prefs.getBoolean("isGuest", false)) {
                startActivity(Intent(this, UserTypeSelectActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MethodSelectActivity::class.java))
                finish()
            }
        }
    }
}