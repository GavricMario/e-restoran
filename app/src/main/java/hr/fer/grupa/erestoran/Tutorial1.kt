package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R.layout.activity_tutorial_1
import kotlinx.android.synthetic.main.activity_tutorial_1.*

class Tutorial1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_tutorial_1)

        dalje.setOnClickListener{
            val intent= Intent(this,Tutorial2::class.java)
            startActivity(intent)


        }
        skip_tutorial1.setOnClickListener{
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
