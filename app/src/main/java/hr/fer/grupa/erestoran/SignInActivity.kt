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
import hr.fer.grupa.erestoran.models.Food
import hr.fer.grupa.erestoran.models.Restaurant
import hr.fer.grupa.erestoran.models.User
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        initFirebase()
        initListeners()
    }

    private fun initFirebase() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
//        addRestaurants()
//        addFood()
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
            intent.putExtra("isGuest", true)
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
                            // TODO save user data so he is logged in when app turns on again

                            database.reference.child("Users")
                                                .orderByChild("email")
                                                .equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(object: ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            sessionUser = p0.children.first().getValue(
                                                User::class.java) ?: User()
                                        }

                                        override fun onCancelled(p0: DatabaseError) {
                                            //DO NOTHING
                                        }

                                    })

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

    override fun onBackPressed() {
        super.onBackPressed()
        val intent= Intent(this,MainActivity::class.java)
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
