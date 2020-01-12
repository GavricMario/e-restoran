package hr.fer.grupa.erestoran.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import kotlinx.android.synthetic.main.activity_zahvala.*


class Zahvala : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zahvala)

        initListeners()
    }

    private fun initListeners() {

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        ocjena.setOnClickListener{
            if (ratingBar.rating != 0f) {
                Toast.makeText(this,  "Hvala Vam na povratnoj informaciji", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this,  "Molimo Vas ocijenite Nas", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dostava_restoran.setOnClickListener {
            if (ratingBar.rating != 0f) {
                if (prefs.getBoolean("isGuest", false)) {
                    val intent = Intent(this, UserTypeSelectActivity::class.java)
                    startActivity(intent)
                }
                val intent = Intent(this, MethodSelectActivity::class.java)
                startActivity(intent)
            }
        }



    }

}
