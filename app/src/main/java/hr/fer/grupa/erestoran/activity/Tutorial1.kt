package hr.fer.grupa.erestoran.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_1
import kotlinx.android.synthetic.main.activity_tutorial_1.*
import android.view.animation.TranslateAnimation




class Tutorial1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_tutorial_1)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        dalje.setOnClickListener{
            val intent= Intent(this, Tutorial2::class.java)
            startActivity(intent)
            finish()

        }
        skip_tutorial1.setOnClickListener{
            prefs.edit().putBoolean("firstUse", true).apply()
            onBackPressed()
        }


            for(x in 0 until 10){
            val moveDownToUp = TranslateAnimation(0f, 0f, 0f, 400f)
            moveDownToUp.setDuration(1000)
            moveDownToUp.setFillAfter(true)

            prst.startAnimation(moveDownToUp)

            val moveUpToDown = TranslateAnimation(0f, 0f, 0f, -400f)
            moveUpToDown.setDuration(1000)
            moveUpToDown.setFillAfter(true)
            prst.startAnimation(moveUpToDown)
        }



//        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
//        animator.repeatCount = ValueAnimator.INFINITE
//        animator.interpolator = LinearInterpolator()
//        animator.duration = 9000L
//        animator.addUpdateListener { animation ->
//            val progress = animation.animatedValue as Float
//            val length = prst.height.toFloat()
//            val translationY = length * progress *35
//            prst.translationY= translationY
//
//        }
//        animator.start()

    }

}
