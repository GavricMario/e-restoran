package hr.fer.grupa.erestoran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_1
import kotlinx.android.synthetic.main.activity_tutorial_1.*

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
    }

}
