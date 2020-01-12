package hr.fer.grupa.erestoran.orderhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid
import kotlinx.android.synthetic.main.activity_order_history.*

class OrderHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        backButton.setOnClickListener {
            onBackPressed()
        }

        orderRecycleView.layoutManager = LinearLayoutManager(this)

        val adapter = if (sessionUser.orderHistory.size != 0 ) {
            noItemText.visibility = View.GONE
            OrderAdapter(this, sessionUser.orderHistory)
        } else {
            OrderAdapter(this, ArrayList())
        }
        orderRecycleView.adapter = adapter
    }
}
