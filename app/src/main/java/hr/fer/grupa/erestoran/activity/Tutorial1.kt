package hr.fer.grupa.erestoran.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_1
import kotlinx.android.synthetic.main.activity_tutorial_1.*
import android.view.animation.TranslateAnimation
import android.view.animation.Animation



import android.view.animation.LinearInterpolator










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

        prst.setVisibility(View.VISIBLE)
         var  mAnimation = TranslateAnimation(
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.ABSOLUTE, -1000f
        )
        mAnimation.setDuration(2000)
        mAnimation.setRepeatCount(-1)
        mAnimation.setRepeatMode(Animation.REVERSE)
        mAnimation.setInterpolator(LinearInterpolator())
        prst.setAnimation(mAnimation)





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
