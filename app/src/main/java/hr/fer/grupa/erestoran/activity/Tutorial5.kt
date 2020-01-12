package hr.fer.grupa.erestoran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_5
import kotlinx.android.synthetic.main.activity_tutorial_5.*

class Tutorial5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_5)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)


        var  mAnimation =AnimationUtils.loadAnimation(this, R.anim.shake)
        mAnimation.setRepeatCount(10)

        mobitel.startAnimation(mAnimation)


        povratak.setOnClickListener{
            prefs.edit().putBoolean("firstUse", true).apply()

            val intent = Intent(this, UserTypeSelectActivity::class.java)
            startActivity(intent)


        }
        skip_tutorial5.setOnClickListener{
            onBackPressed()
        }
    }

}
