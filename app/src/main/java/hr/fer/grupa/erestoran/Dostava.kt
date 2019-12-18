package hr.fer.grupa.erestoran

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dostava.*


class Dostava : AppCompatActivity() {

    var radioGroup: RadioGroup? = null
    var radioButton: RadioButton? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dostava)

        radioGroup = findViewById(R.id.radio_Group);
        val radioId = radioGroup!!.getCheckedRadioButtonId()

        radioButton = findViewById(radioId)


        potvrdi_id.setOnClickListener(){
            if (kartica.isChecked){
                val intent= Intent(this,CardPaymentUnregistered::class.java)
                startActivity(intent)
            }
            else{
                val intent= Intent(this,Zahvala::class.java)
                startActivity(intent)
            }
        }


    }
}
