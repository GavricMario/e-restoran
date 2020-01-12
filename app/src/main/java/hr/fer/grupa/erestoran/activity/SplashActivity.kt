package hr.fer.grupa.erestoran.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.Order
import hr.fer.grupa.erestoran.models.User
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initFirebase()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            checkLogin()
        }
    }

    private fun checkLogin() {
        prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
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

                                    for (valueRes in p0.children.first().child("orders").children) {
                                        val order = valueRes.getValue(Order::class.java)
                                        Log.d("Test", "Test")
                                        if(order != null) {
                                            sessionUser.orderHistory.add(order)
                                        }
                                    }

                                    userUid = p0.children.first().key ?: ""
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                    //DO NOTHING
                                }

                            })
                        prefs.edit().putBoolean("isGuest", false).apply()
                        startActivity(Intent(this, MethodSelectActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, UserTypeSelectActivity::class.java))
                        finish()
                    }
                } else {
                    prefs.edit().clear().apply()
                    startActivity(Intent(this, UserTypeSelectActivity::class.java))
                    finish()
                }
            }

    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            123
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkLogin()
    }
}
