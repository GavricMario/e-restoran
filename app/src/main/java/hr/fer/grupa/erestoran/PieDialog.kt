package hr.fer.grupa.erestoran

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window.FEATURE_NO_TITLE
import com.ramotion.circlemenu.CircleMenuView
import hr.fer.grupa.erestoran.activity.Jezik
import hr.fer.grupa.erestoran.activity.MethodSelectActivity
import hr.fer.grupa.erestoran.drink.DrinkFragment
import hr.fer.grupa.erestoran.food.FoodFragment
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
                when(index) {
                    0 -> {
                        activityContext.startActivity(Intent(activityContext, MethodSelectActivity::class.java))
                        (activityContext as OrderBaseActivity).finish()
                    }
                    1 -> {
                        circleMenu.close(true)
                        (activityContext as OrderBaseActivity).supportFragmentManager.beginTransaction().add(R.id.container, RestaurantsFragment(), "restaurant")
                            .addToBackStack("restaurant").setCustomAnimations(
                                R.anim.fragment_enter_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_exit_animation
                            ).commit()
                        dialogContext.dismiss()
                    }
                    2 -> {
                        circleMenu.close(true)
                        (activityContext as OrderBaseActivity).supportFragmentManager.beginTransaction().add(R.id.container, FoodFragment(), "food")
                            .addToBackStack("drink").setCustomAnimations(
                                R.anim.fragment_enter_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_exit_animation
                            ).commit()
                        dialogContext.dismiss()
                    }
                    3 -> {
                        circleMenu.close(true)
                        (activityContext as OrderBaseActivity).supportFragmentManager.beginTransaction().add(R.id.container, DrinkFragment(), "drink")
                            .addToBackStack("drink").setCustomAnimations(
                                R.anim.fragment_enter_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_exit_animation
                            ).commit()
                        dialogContext.dismiss()
                    }
                    4 -> {
                        circleMenu.close(true)
                        (activityContext as OrderBaseActivity).supportFragmentManager.beginTransaction().add(R.id.container, OrderOverviewFragment(), "overview")
                            .addToBackStack("overview").setCustomAnimations(
                                R.anim.fragment_enter_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_stay_animation,
                                R.anim.fragment_exit_animation
                            ).commit()
                        dialogContext.dismiss()
                    }
                }
            }
        }

        languageChangeButton.setOnClickListener {
            val intent = Intent(context, Jezik::class.java)
            context.startActivity(intent)
        }
    }

    override fun show() {
        super.show()
        Handler().postDelayed({
            circleMenu.open(true)
        }, 100)
    }
}