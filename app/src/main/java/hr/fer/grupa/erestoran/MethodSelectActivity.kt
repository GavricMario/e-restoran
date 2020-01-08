package hr.fer.grupa.erestoran

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import hr.fer.grupa.erestoran.order.OrderBaseActivity
import kotlinx.android.synthetic.main.activity_method_select.*


class MethodSelectActivity : AppCompatActivity() {

    private lateinit var menuDialog: MenuDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_method_select)

        val isGuest: Boolean = intent.extras?.getBoolean("isGuest") ?: false

        if (isGuest) {
            order_view.visibility = GONE
            val params = restaurant_view.layoutParams as ConstraintLayout.LayoutParams
            params.height = MATCH_PARENT
            restaurant_view.layoutParams = params
            menuButton.visibility = GONE
            menuButton2.visibility = GONE
            divider.visibility = GONE
        }

        menuDialog = MenuDialog(this)
        menuDialog.setCancelable(true)
        menuDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        setListeners()
    }

    private fun setListeners() {
        order_view.setOnTouchListener(object : OnSwipeTouchListener(this){
            override fun onSwipeDown() {
                restaurant_view.visibility = GONE
                val params = order_view.layoutParams as ConstraintLayout.LayoutParams
                params.height = MATCH_PARENT
                order_view.layoutParams = params
                menuButton.visibility = GONE
                menuButton2.visibility = GONE
                divider.visibility = GONE
                navigate("order")
            }
        })

        restaurant_view.setOnTouchListener(object : OnSwipeTouchListener(this){
            override fun onSwipeUp() {
                order_view.visibility = GONE
                val params = restaurant_view.layoutParams as ConstraintLayout.LayoutParams
                params.height = MATCH_PARENT
                restaurant_view.layoutParams = params
                menuButton.visibility = GONE
                menuButton2.visibility = GONE
                divider.visibility = GONE
                navigate("restaurant")
            }
        })

        menuButton.setOnClickListener {
            menuDialog.show()
        }

        menuButton2.setOnClickListener {
            menuDialog.show()
        }
    }

    private fun navigate(action: String) {
        Handler().postDelayed({
            when (action) {
                "order" -> startActivity(Intent(this, HomeActivity::class.java).putExtra("type", "order"))
                "restaurant" -> startActivity(Intent(this, OrderBaseActivity::class.java))
            }
        }, 500)

        //TODO change activities
    }
}