package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.id
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tutorijal.setOnClickListener{
            val intent= Intent(this,Tutorial_1::class.java)
            startActivity(intent)


        }
        jezici.setOnClickListener{
            val intent2=Intent(this, Jezik::class.java)
            startActivity(intent2)
        }

    }
}
