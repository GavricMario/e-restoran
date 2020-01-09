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
import kotlinx.android.synthetic.main.pie_menu.*


class PieDialog(context: Context) : Dialog(context, android.R.style.Theme_Light) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        setContentView(R.layout.pie_menu)

        Handler().postDelayed({
            circleMenu.open(true)
        }, 100)

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
                this@PieDialog.dismiss()
            }

            override fun onButtonClickAnimationStart(view: CircleMenuView, index: Int) {
                Log.d("D", "onButtonClickAnimationStart| index: $index")
            }

            override fun onButtonClickAnimationEnd(view: CircleMenuView, index: Int) {
                Log.d("D", "onButtonClickAnimationEnd| index: $index")
            }
        }

        languageChangeButton.setOnClickListener {
            val intent = Intent(context, Jezik::class.java)
            context.startActivity(intent)
        }
    }
}