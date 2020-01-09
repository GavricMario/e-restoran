package hr.fer.grupa.erestoran.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import kotlinx.android.synthetic.main.activity_main.*

class UserTypeSelectActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        val firstUse = prefs.getBoolean("firstUse", false)
        if (!firstUse) {
            startActivity(Intent(this,
                Tutorial1::class.java))
        }

        setListeners()
    }

    private fun setListeners() {
        tutorijal.setOnClickListener{
            val intent = Intent(this, Tutorial1::class.java)
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
            val intent = Intent(this, MethodSelectActivity::class.java)
            prefs.edit()
                .putBoolean("isGuest", true)
                .apply()
            startActivity(intent)
            finish()
        }
    }
}
