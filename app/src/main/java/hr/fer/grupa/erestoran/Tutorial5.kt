package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_5
import kotlinx.android.synthetic.main.activity_tutorial_1.*
import kotlinx.android.synthetic.main.activity_tutorial_5.*

class Tutorial5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_5)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        povratak.setOnClickListener{
            prefs.edit().putBoolean("firstUse", true).apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }
        skip_tutorial5.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
