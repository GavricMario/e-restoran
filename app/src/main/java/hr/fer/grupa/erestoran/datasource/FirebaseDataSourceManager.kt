package hr.fer.grupa.erestoran.datasource

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.AddressModel
import hr.fer.grupa.erestoran.models.Order
import hr.fer.grupa.erestoran.models.RatingModel
import hr.fer.grupa.erestoran.models.User
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid

class FirebaseDataSourceManager{

    companion object {

        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
        private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        private lateinit var instance: FirebaseDataSourceManager
        private lateinit var userListener: ValueEventListener

        fun getInstance(): FirebaseDataSourceManager {
            if (!::instance.isInitialized) {
                instance = FirebaseDataSourceManager()

                userListener = database.reference.child("Users").child(userUid).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        sessionUser = p0.getValue(User::class.java) ?: sessionUser

                        for (valueRes in p0.child("orders").children) {
                            val order = valueRes.getValue(Order::class.java)
                            Log.d("Test", "Test")
                            if(order != null) {
                                sessionUser.orderHistory.add(order)
                            }
                        }

                        for (valueRes in p0.child("address").children) {
                            val order = valueRes.getValue(AddressModel::class.java)
                            if(order != null) {
                                sessionUser.addresses.add(order)
                            }
                        }

                        userUid = p0.key ?: ""
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
            }

            return instance
        }
    }

    fun saveAddress(address: AddressModel) {
        val key = database.reference.child("Users").child(userUid).child("addresses").push().key
        if (key != null ) {
            database.reference.child("Users").child(userUid).child("addresses").child(key).setValue(address)
        }
    }

    fun setLanguage(language: String) {
        if (userUid != "") {
            database.reference.child("Users").child(userUid).child("language").setValue(language)
        }
    }

    fun saveOrder(order: Order) {
        val key = database.reference.child("Users").child(userUid).child("orders").push().key
        if (key != null ) {
            database.reference.child("Users").child(userUid).child("orders").child(key).setValue(order)
        }
    }

    fun resetPassword(email: String, context: Context) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, context.getString(R.string.password_reset_text), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, context.getString(R.string.something_went_wrong_error), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveRating(rating: RatingModel) {
        val key = database.reference.child("Ratings").push().key
        if (key != null ) {
            database.reference.child("Ratings").child(key).setValue(rating)
        }
    }
}