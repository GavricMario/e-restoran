package hr.fer.grupa.erestoran.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import kotlinx.android.synthetic.main.activity_dostava.*


class Dostava : AppCompatActivity() {

    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dostava)

        radioGroup = findViewById(R.id.radio_Group)
        val radioId = radioGroup!!.checkedRadioButtonId

        radioButton = findViewById(radioId)


        potvrdi_id.setOnClickListener {
            if (kartica.isChecked){
                val intent= Intent(this,
                    Payment::class.java)
                startActivity(intent)
            }
            else{
                val intent= Intent(this, Zahvala::class.java)
                startActivity(intent)
            }
        }


    }
}
