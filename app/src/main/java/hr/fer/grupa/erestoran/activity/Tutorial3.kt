package hr.fer.grupa.erestoran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_3
import kotlinx.android.synthetic.main.activity_tutorial_1.*
import kotlinx.android.synthetic.main.activity_tutorial_3.*
import kotlinx.android.synthetic.main.activity_tutorial_3.dalje

class Tutorial3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_3)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)



        prst_gore.setVisibility(View.VISIBLE)
        var  mAnimation = TranslateAnimation(
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.ABSOLUTE, -200f,
            TranslateAnimation.ABSOLUTE, 0f,
            TranslateAnimation.ABSOLUTE, 0f
        )
        mAnimation.setDuration(900)
        mAnimation.setRepeatCount(-1)
        mAnimation.setRepeatMode(Animation.RESTART)
        mAnimation.setInterpolator(LinearInterpolator())
        prst_gore.setAnimation(mAnimation)

        dalje.setOnClickListener{
            val intent= Intent(this, Tutorial4::class.java)
            startActivity(intent)
            finish()

        }
        skip_tutorial3.setOnClickListener{
            prefs.edit().putBoolean("firstUse", true).apply()
            onBackPressed()
        }
    }

}
