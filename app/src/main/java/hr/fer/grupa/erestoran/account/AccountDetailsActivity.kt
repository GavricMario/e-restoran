package hr.fer.grupa.erestoran.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.datasource.FirebaseDataSourceManager
import hr.fer.grupa.erestoran.menu.InfiniteAdapter
import hr.fer.grupa.erestoran.models.AddressModel
import hr.fer.grupa.erestoran.util.sessionUser
import kotlinx.android.synthetic.main.activity_account_details.*
import kotlinx.android.synthetic.main.custom_menu_dialog.*

class AccountDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)

        firstNameText.text = sessionUser.firstName
        lastNameText.text = sessionUser.lastName
        usernameText.text = sessionUser.username
        emailText.text = sessionUser.email

        if (sessionUser.addresses.size == 0) {
            noAddressText.visibility = View.VISIBLE
            addressRecycleView.visibility = View.INVISIBLE
        } else {
            noAddressText.visibility = View.INVISIBLE
            addressRecycleView.visibility = View.VISIBLE

            val adapter = AddressAdapter(this, sessionUser.addresses)

            addressRecycleView.layoutManager = LinearLayoutManager(this)
            addressRecycleView.adapter = adapter
        }

        setListeners()
    }

    private fun setListeners() {
        editData.setOnClickListener {
            //nothing yet
        }

        resetPassword.setOnClickListener {
            FirebaseDataSourceManager.getInstance().resetPassword(sessionUser.email, this)
        }

        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
