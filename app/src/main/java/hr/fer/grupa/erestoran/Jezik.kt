package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_jezik.*

class Jezik : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jezik)

        hrv.setOnClickListener{
            sessionUser.language = "hr"
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        eng.setOnClickListener{
            sessionUser.language = "eng"
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)


        }
        de.setOnClickListener{
            sessionUser.language = "de"
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

