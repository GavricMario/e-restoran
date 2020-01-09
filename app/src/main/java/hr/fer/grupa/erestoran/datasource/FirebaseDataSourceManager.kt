package hr.fer.grupa.erestoran.datasource

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.models.AddressModel
import hr.fer.grupa.erestoran.models.User
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid

class FirebaseDataSourceManager{

    companion object {

        private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        private lateinit var instance: FirebaseDataSourceManager
        private lateinit var userListener: ValueEventListener

        fun getInstance(): FirebaseDataSourceManager {
            if (!::instance.isInitialized) {
                instance = FirebaseDataSourceManager()

                userListener = database.reference.child("Users").child(userUid).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(p0: DataSnapshot) {
                        sessionUser = p0.getValue(User::class.java) ?: sessionUser
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
        val key = database.reference.child("Users").child(userUid).child("address").push().key
        if (key != null ) {
            database.reference.child("Users").child(userUid).child("address").child(key).setValue(address)
        }
    }

    fun setLanguage(language: String) {
        if (userUid != "") {
            database.reference.child("Users").child(userUid).child("language").setValue(language)
        }
    }
}