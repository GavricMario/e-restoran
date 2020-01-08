package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_card_payment_unregistered.*

class CardPaymentUnregistered : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment_unregistered)

        placanje.setOnClickListener{
            val str = broj.text.toString().trim()
            if(!checkCreditCard(str)) {
                broj_kartice.error = "Enter valid number"
                broj.requestFocus()
            }else{
                val intent= Intent(this,Zahvala::class.java)
                startActivity(intent)
            }


        }
        nazad.setOnClickListener{
            val intent= Intent(this, Dostava::class.java)
            startActivity(intent)

        }
    }
}
