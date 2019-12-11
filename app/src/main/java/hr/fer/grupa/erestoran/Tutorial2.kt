package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_2
import kotlinx.android.synthetic.main.activity_tutorial_2.*

class Tutorial2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_2)
        dalje.setOnClickListener{
            val intent= Intent(this,Tutorial3::class.java)
            startActivity(intent)


        }
    }
}
