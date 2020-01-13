package hr.fer.grupa.erestoran.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.account.AddAddressActivity
import hr.fer.grupa.erestoran.account.AddressAdapter
import hr.fer.grupa.erestoran.util.sessionUser
import kotlinx.android.synthetic.main.activity_dostava.*
import androidx.recyclerview.widget.RecyclerView
import hr.fer.grupa.erestoran.models.AddressModel


class Dostava : AppCompatActivity() {

    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null

    private lateinit var address: ArrayList<AddressModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dostava)

        address = sessionUser.addresses

        radioGroup = findViewById(R.id.radio_Group)
        val radioId = radioGroup!!.checkedRadioButtonId

        radioButton = findViewById(radioId)

        potvrdi_id.setOnClickListener {
            if (address.size == 0) {
                Toast.makeText(this, getString(R.string.add_address_message), Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, Zahvala::class.java)
                startActivity(intent)
                finish()
            }
        }

        odustani_id.setOnClickListener {
            val intent = Intent(this, MethodSelectActivity::class.java)
            startActivity(intent)
            finish()
        }

        val addresses: Array<String> = Array(size = address.size, init = {""})
        for (i in 0 until address.size) {
            val text = "${address[i].streetName} ${address[i].streetNumber}, ${address[i].city} ${address[i].postalCode}"
            addresses[i] = text
        }

        numberPicker.maxValue = addresses.size - 1
        numberPicker.displayedValues = addresses

    }

    override fun onResume() {
        super.onResume()
        address = sessionUser.addresses
    }
}
