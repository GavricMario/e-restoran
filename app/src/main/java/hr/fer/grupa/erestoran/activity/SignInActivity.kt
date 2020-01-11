package hr.fer.grupa.erestoran.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.*
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        initFirebase()
        initListeners()

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        val email = prefs.getString("email", "") ?: ""
        val password = prefs.getString("password", "") ?: ""
        if (email != "") {
            emailLogin(email, password)
        }
    }

    private fun initFirebase() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
//        addRestaurants()
//        addFood()
//        addDrinks()
    }

    // Function for adding restaurant data to firebase
    private fun addRestaurants() {
        val restaurant = Restaurant()
        restaurant.title = "La Bodega"
        restaurant.lat = 45.812133
        restaurant.long = 15.975118
        restaurant.description = "La Bodega je finger food restoran s velikim izborom pršuta, sireva, kobasica, kulena, maslina i maslinovog ulja."
        restaurant.address = "Bogovićeva ul. 5, Zagreb"
        database.reference.child("Restaurants").child(UUID.randomUUID().toString()).setValue(restaurant)
        restaurant.title = "Hanami"
        restaurant.lat = 45.819005
        restaurant.long = 15.978193
        restaurant.description = "Restoran Hanami gostima nudi specijalitete azijske kuhinje poput sake nigirija te tuna sashimijs, kao i ramen te slična jela."
        restaurant.address = "Nova ves 4, Zagreb"
        database.reference.child("Restaurants").child(UUID.randomUUID().toString()).setValue(restaurant)
    }

    // Function for adding food data to firebase
    private fun addFood() {
        val food = Food()
        food.title = "Juha"
        food.subtitle = "Juha od gljiva"
        food.type = "predjelo"
        food.price = 20.00f
        database.reference.child("Food").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Naresci"
        food.subtitle = "Prsut, sir"
        food.type = "predjelo"
        food.price = 15.00f
        database.reference.child("Food").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Mesna plata"
        food.subtitle = "Pljeskavice, cevapi"
        food.type = "glavno"
        food.price = 50.00f
        database.reference.child("Food").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Kolac"
        food.subtitle = "Od oraha"
        food.type = "desert"
        food.price = 10.00f
        database.reference.child("Food").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
    }
    // Function for adding drink data to firebase
    private fun addDrinks() {
        val food = Drink()
        food.title = "Coca cola"
        food.subtitle = "Gazirano"
        food.type = "bezalkoholno"
        food.price = 20.00f
        database.reference.child("Drink").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Voda"
        food.subtitle = "Obicna voda"
        food.type = "bezalkoholno"
        food.price = 15.00f
        database.reference.child("Drink").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Kozel"
        food.subtitle = "Tamni"
        food.type = "alkoholno"
        food.price = 20.00f
        database.reference.child("Drink").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Kozel"
        food.subtitle = "Svijetli"
        food.type = "alkoholno"
        food.price = 20.00f
        database.reference.child("Drink").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Kava"
        food.subtitle = "Crna"
        food.type = "toplo"
        food.price = 10.00f
        database.reference.child("Drink").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Čaj"
        food.subtitle = "Voćni"
        food.type = "toplo"
        food.price = 10.00f
        database.reference.child("Drink").child("3e092f4e-030a-415a-9308-90be8df7801f").child(UUID.randomUUID().toString()).setValue(food)
    }

    private fun initListeners() {
        register_btn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        signIn_btn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            loginUser()
        }
        guest_btn.setOnClickListener {
            val intent = Intent(this, MethodSelectActivity::class.java)
            prefs.edit()
                .putBoolean("isGuest", true)
                .apply()
            startActivity(intent)
        }
    }

    private fun loginUser(){
        val emailUsername = email_username_text.text.toString().trim()
        val password = password_text.text.toString().trim()
        if(emailUsername.isEmpty()){
            email_username_text.error = "Enter Email or Username"
            email_username_text.requestFocus()
            progressBar.visibility = View.GONE
        } else if(password.isEmpty()){
            password_text.error = "Enter password"
            password_text.requestFocus()
            progressBar.visibility = View.GONE
        }else if(emailUsername.contains('@')){
            emailLogin(emailUsername, password)
        }else{
            usernameLogin(emailUsername, password)
        }

    }
    private fun emailLogin(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val isVerified = it.result?.user?.isEmailVerified ?: false
                        if (isVerified) {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Signed in successfully!", Toast.LENGTH_SHORT).show()

                            database.reference.child("Users")
                                                .orderByChild("email")
                                                .equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(object: ValueEventListener {
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
                            saveCredentials(email, password)
                            prefs.edit().putBoolean("isGuest", false).apply()
                            startActivity(Intent(this, MethodSelectActivity::class.java))
                            finish()
                        } else {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this, "Please verify your email first", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }

    }

    private fun saveCredentials(email: String, password: String) {
        val prefs: SharedPreferences = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this, UserTypeSelectActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun usernameLogin(username: String,password: String) {
        var uid = ""
        database.reference.child("Usernames").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.hasChild(username)){
                    uid = p0.child(username).value.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                //DO NOTHING
            }
        })

        var email: String
        database.reference.child("Users").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                email = p0.child(uid).child("email").value.toString()
                emailLogin(email, password)
            }

            override fun onCancelled(p0: DatabaseError) {
                //DO NOTHING
            }
        })


    }
}
