package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.layout.activity_tutorijal_4
import kotlinx.android.synthetic.main.activity_tutorijal_4.*

class Tutorijal_4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_tutorijal_4)
        dalje4.setOnClickListener{
            val intent= Intent(this,Tutorial_5::class.java)
            startActivity(intent)


        }
    }
}
