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
            onBackPressed()
        }
        eng.setOnClickListener{
            sessionUser.language = "eng"
            onBackPressed()
        }
        de.setOnClickListener{
            sessionUser.language = "de"
            onBackPressed()
        }
    }

}

