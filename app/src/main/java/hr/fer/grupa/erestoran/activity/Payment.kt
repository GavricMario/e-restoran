package hr.fer.grupa.erestoran.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R
import kotlinx.android.synthetic.main.activity_payment.*

class Payment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        kartica.setOnClickListener{
            val intent= Intent(this, Zahvala::class.java)
            startActivity(intent)
        }

        cash.setOnClickListener{
            val intent= Intent(this, Zahvala::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener{
            onBackPressed()

        }
    }
}
