package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_3
import kotlinx.android.synthetic.main.activity_tutorial_3.*

class Tutorial3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorial_3)
        dalje.setOnClickListener{
            val intent= Intent(this,Tutorial4::class.java)
            startActivity(intent)


        }
    }
}
