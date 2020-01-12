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



class Dostava : AppCompatActivity() {

    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null

    private lateinit var adapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dostava)

        radioGroup = findViewById(R.id.radio_Group)
        val radioId = radioGroup!!.checkedRadioButtonId

        radioButton = findViewById(radioId)

        potvrdi_id.setOnClickListener {
            if (adapter.itemCount == 0) {
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

        adapter = AddressAdapter(this, sessionUser.addresses)
        val layoutManager = LinearLayoutManager(this)

        addressRecyclerView.layoutManager = layoutManager
        addressRecyclerView.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(addressRecyclerView)

        addressRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager)
                    val pos = layoutManager.getPosition(centerView!!)
                    (recyclerView.adapter as AddressAdapter).setSnappedPosition(pos)
                }
            }
        })
        if (adapter.itemCount > 0)
        addressRecyclerView.scrollToPosition(adapter.itemCount / 2)

        addAddressButton.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        adapter = AddressAdapter(this, sessionUser.addresses)
        addressRecyclerView.adapter = adapter
    }
}
