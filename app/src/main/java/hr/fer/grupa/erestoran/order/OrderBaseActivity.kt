package hr.fer.grupa.erestoran.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.databinding.OrderBaseActivityBinding
import hr.fer.grupa.erestoran.drink.DrinkFragment
import hr.fer.grupa.erestoran.food.FoodFragment
import hr.fer.grupa.erestoran.models.*
import hr.fer.grupa.erestoran.overview.OrderOverviewFragment
import hr.fer.grupa.erestoran.restaurants.RestaurantsFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrderBaseActivity : AppCompatActivity() {

    private lateinit var binding: OrderBaseActivityBinding

    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.order_base_activity)
        binding.activity = this
        val restaurantPickFragment = RestaurantsFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, restaurantPickFragment, "restaurant")
            .addToBackStack("restaurant").commit()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFragmentEvent(event: OrderFragmentEvent) {
        when (event.fragment) {
            is RestaurantsFragment -> {
                order =
                    Order(event.data as Restaurant, mutableSetOf(), mutableSetOf(), mutableListOf())
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
                order.food = event.data as MutableSet<Food>
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
                order.drink = event.data as MutableSet<Drink>
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

    override fun onBackPressed() {
        if (supportFragmentManager.fragments.size > 1) supportFragmentManager.popBackStack()
        else super.onBackPressed()
    }
}