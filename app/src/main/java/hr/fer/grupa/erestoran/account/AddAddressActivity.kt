package hr.fer.grupa.erestoran.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.datasource.FirebaseDataSourceManager
import hr.fer.grupa.erestoran.models.AddressModel
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        initListeners()
    }

    private fun initListeners() {
        backButton.setOnClickListener {
            onBackPressed()
        }

        buttonAdd.setOnClickListener{
            val streetName = streetNameText.text.trim().toString()
            val streetNumber = streetNumberText.text.trim().toString()
            val city = cityText.text.trim().toString()
            val postalCode = postalCodeText.text.trim().toString()
            val specialNote = notesText.text.trim().toString()

            when {
                streetName == "" -> {
                    streetNameText.requestFocus()
                    streetNameText.error = "Field necessaryy"
                }
                streetNumber == "" -> {
                    streetNumberText.requestFocus()
                    streetNumberText.error = "Field necessaryy"
                }
                city == "" -> {
                    cityText.requestFocus()
                    cityText.error = "Field necessaryy"
                }
                postalCode == "" -> {
                    postalCodeText.requestFocus()
                    postalCodeText.error = "Field necessaryy"
                }
                else -> {
                    val address = AddressModel(
                        streetName = streetName,
                        streetNumber = streetNumber,
                        city = city,
                        postalCode = postalCode,
                        specialNotes = specialNote
                    )
                    FirebaseDataSourceManager.getInstance().saveAddress(address)
                    onBackPressed()
                }
            }
        }
    }
}
