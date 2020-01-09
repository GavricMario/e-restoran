package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_sign_in.*

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initFirebase()

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        val email = prefs.getString("email", "") ?: ""
        val password = prefs.getString("password", "") ?: ""
        if (email != "") {
            emailLogin(email, password)
        } else {
            startActivity(Intent(this, UserTypeSelectActivity::class.java))
            finish()
        }
    }

    private fun initFirebase() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
//        addRestaurants()
//        addFood()
    }

    private fun emailLogin(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val isVerified = it.result?.user?.isEmailVerified ?: false
                    if (isVerified) {
                        Toast.makeText(this, "Signed in successfully!", Toast.LENGTH_SHORT).show()

                        database.reference.child("Users")
                            .orderByChild("email")
                            .equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(object:
                                ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    sessionUser = p0.children.first().getValue(User::class.java) ?: User()
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                    //DO NOTHING
                                }

                            })
                        startActivity(Intent(this, MethodSelectActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, UserTypeSelectActivity::class.java))
                        finish()
                    }
                } else {
                    startActivity(Intent(this, UserTypeSelectActivity::class.java))
                    finish()
                }
            }

    }
}
