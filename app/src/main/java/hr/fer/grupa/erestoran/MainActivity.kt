package hr.fer.grupa.erestoran

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        val firstUse = prefs.getBoolean("firstUse", false)
        if (!firstUse) {
            startActivity(Intent(this,Tutorial1::class.java))
            finish()
        }

        setListeners()
    }

    private fun setListeners() {
        tutorijal.setOnClickListener{
            val intent = Intent(this,Tutorial1::class.java)
            startActivity(intent)


        }
        jezici.setOnClickListener{
            val intent = Intent(this, Jezik::class.java)
            startActivity(intent)
        }

        prijava.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            finish()
        }

        gost.setOnClickListener{
            val intent = Intent(this, Zahvala::class.java)
            intent.putExtra("isGuest", true)
            startActivity(intent)

            finish()
        }
    }
}
