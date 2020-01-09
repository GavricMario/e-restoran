package hr.fer.grupa.erestoran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_3
import kotlinx.android.synthetic.main.activity_tutorial_3.*
import kotlinx.android.synthetic.main.activity_tutorial_3.dalje

class Tutorial3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_3)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

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
