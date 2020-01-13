package hr.fer.grupa.erestoran.menu

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window.FEATURE_NO_TITLE
import android.widget.Toast
import com.ramotion.circlemenu.CircleMenuView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.activity.Jezik
import hr.fer.grupa.erestoran.activity.MethodSelectActivity
import hr.fer.grupa.erestoran.drink.DrinkFragment
import hr.fer.grupa.erestoran.food.FoodFragment
import hr.fer.grupa.erestoran.models.Order
import hr.fer.grupa.erestoran.order.OrderBaseActivity
import hr.fer.grupa.erestoran.overview.OrderOverviewFragment
import hr.fer.grupa.erestoran.restaurants.RestaurantsFragment
import kotlinx.android.synthetic.main.pie_menu.*


class PieDialog(private val activityContext: Context) : Dialog(activityContext, android.R.style.Theme_Light) {

    private lateinit var dialogContext: PieDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        setContentView(R.layout.pie_menu)

        dialogContext = this

        circleMenu.eventListener = object : CircleMenuView.EventListener() {
            override fun onMenuOpenAnimationStart(view: CircleMenuView) {
                Log.d("D", "onMenuOpenAnimationStart")
            }

            override fun onMenuOpenAnimationEnd(view: CircleMenuView) {
                Log.d("D", "onMenuOpenAnimationEnd")
            }

            override fun onMenuCloseAnimationStart(view: CircleMenuView) {
                Log.d("D", "onMenuCloseAnimationStart")
            }

            override fun onMenuCloseAnimationEnd(view: CircleMenuView) {
                dialogContext.dismiss()
            }

            override fun onButtonClickAnimationStart(view: CircleMenuView, index: Int) {
                Log.d("D", "onButtonClickAnimationStart| index: $index")
            }

            override fun onButtonClickAnimationEnd(view: CircleMenuView, index: Int) {
                val order = (activityContext as OrderBaseActivity).getOrder()
                when(index) {
                    0 -> {
                        activityContext.startActivity(Intent(activityContext, MethodSelectActivity::class.java))
                        activityContext.finish()
                    }
                    1 -> {
                        circleMenu.close(true)
                        activityContext.supportFragmentManager.beginTransaction().add(
                            R.id.container, RestaurantsFragment(), "restaurant")
                            .addToBackStack("restaurant").setCustomAnimations(
                                R.anim.fragment_enter_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_exit_animation
                            ).commit()
                        dialogContext.dismiss()
                    }
                    2 -> {
                        if (order.restaurant.id != "") {
                            circleMenu.close(true)
                            val fragment = FoodFragment()
                            val bundle = Bundle()
                            bundle.putString("restaurant", order.restaurant.id)
                            fragment.arguments = bundle
                            activityContext.supportFragmentManager.beginTransaction().add(
                                R.id.container, fragment, "food")
                                .addToBackStack("food").setCustomAnimations(
                                    R.anim.fragment_enter_animation,
                                    R.anim.fragment_stay_animation,
                                    R.anim.fragment_stay_animation,
                                    R.anim.fragment_exit_animation
                                ).commit()
                        } else {
                            Toast.makeText(context, context.getString(R.string.select_restaurant_message), Toast.LENGTH_LONG).show()
                        }
                        dialogContext.dismiss()
                    }
                    3 -> {
                        if (order.restaurant.id != "") {
                            circleMenu.close(true)
                            val fragment = DrinkFragment()
                            val bundle = Bundle()
                            bundle.putString("restaurant", order.restaurant.id)
                            fragment.arguments = bundle
                            activityContext.supportFragmentManager.beginTransaction()
                                .add(R.id.container, fragment, "drink")
                                .addToBackStack("drink").setCustomAnimations(
                                    R.anim.fragment_enter_animation,
                                    R.anim.fragment_stay_animation,
                                    R.anim.fragment_stay_animation,
                                    R.anim.fragment_exit_animation
                                ).commit()
                        } else {
                            Toast.makeText(context, context.getString(R.string.select_restaurant_message), Toast.LENGTH_LONG).show()
                        }
                        dialogContext.dismiss()
                    }
                    4 -> {
                        if (order.restaurant.id != "") {
                            circleMenu.close(true)
                            val fragment = OrderOverviewFragment()
                            val bundle = Bundle()
                            bundle.putSerializable("order", order)
                            fragment.arguments = bundle
                            activityContext.supportFragmentManager.beginTransaction()
                                .add(R.id.container, fragment, "overview")
                                .addToBackStack("overview").setCustomAnimations(
                                    R.anim.fragment_enter_animation,
                                    R.anim.fragment_stay_animation,
                                    R.anim.fragment_stay_animation,
                                    R.anim.fragment_exit_animation
                                ).commit()
                        } else {
                            Toast.makeText(context, context.getString(R.string.select_restaurant_message), Toast.LENGTH_LONG).show()
                        }
                        dialogContext.dismiss()
                    }
                }
            }
        }
    }

    override fun show() {
        super.show()
        Handler().postDelayed({
            circleMenu.open(true)
        }, 100)
    }
}