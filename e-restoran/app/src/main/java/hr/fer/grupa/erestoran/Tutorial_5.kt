package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_5
import kotlinx.android.synthetic.main.activity_tutorial_5.*

class Tutorial_5 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_5)
        povratak.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)


        }
    }
}
