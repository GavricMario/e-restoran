package hr.fer.grupa.erestoran.activity

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
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
import hr.fer.grupa.erestoran.models.*
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid
import java.util.*

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
//        addDrinks()
    }

    private fun addFood() {
        val food = Food()
        food.title = "Juha"
        food.subtitle = "Juha od povrća"
        food.type = "predjelo"
        food.price = 20.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/food%2Fjuha_od_povrca.jpeg?alt=media&token=b8f76531-140b-4fdc-848a-f326e304241c"
        food.nutririonValues = NutritionValues("39", "2.6 g", "3.3 g", "340 mg", "31 mg", "0.7 g")
        database.reference.child("Food").child("051bf422-42b5-49ec-ad83-14d0d3ff00e9").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Naresci"
        food.subtitle = "Pršut, sir, kulen"
        food.type = "predjelo"
        food.price = 15.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/food%2Fnaresci.jpg?alt=media&token=fa2514e6-0073-4d7f-b293-bd1cf368b959"
        food.nutririonValues = NutritionValues("272.6", "9.6 g", "20.1 g", "1,581.6 mg", "584.0 mg", "29.5 g")
        database.reference.child("Food").child("051bf422-42b5-49ec-ad83-14d0d3ff00e9").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Tjestenina"
        food.subtitle = "Sa škampima"
        food.type = "glavno"
        food.price = 50.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/food%2Fskampi.jpg?alt=media&token=cef5f04a-3863-4c60-8848-44d2b3c7133f"
        food.nutririonValues = NutritionValues("341.5", "7 g", "33.3 g", "325.3 mg", "626.1 mg", "36 g")
        database.reference.child("Food").child("051bf422-42b5-49ec-ad83-14d0d3ff00e9").child(UUID.randomUUID().toString()).setValue(food)
        food.title = "Kolač"
        food.subtitle = "Od kokosa"
        food.type = "desert"
        food.price = 10.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/food%2Fkolac_od_kokosa.jpg?alt=media&token=83e007a3-7e8a-4c8f-88cc-6813f937cae3"
        food.nutririonValues = NutritionValues("405", "17.3 g", "55.8 g", "200 mg", "0 mg", "5.5 g")
        database.reference.child("Food").child("051bf422-42b5-49ec-ad83-14d0d3ff00e9").child(UUID.randomUUID().toString()).setValue(food)
    }

    private fun addDrinks() {
        val food = Drink()
        food.title = "Coca cola"
        food.subtitle = "Gazirano"
        food.type = "bezalkoholno"
        food.price = 20.00f
        food.extras.add(ExtraFood("Limun", 0f))
        food.extras.add(ExtraFood("Led", 0f))
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/drink%2Fcoca_cola.jpg?alt=media&token=c46e18ed-eaf8-488a-80b8-2c3509761fdf"
        food.nutririonValues = NutritionValues("41", "0 g", "11 g", "4 mg", "3 mg", "0 g")
        database.reference.child("Drink").child("792b275f-9b77-4126-b430-c8462dc7acd7").child(UUID.randomUUID().toString()).setValue(food)
        food.extras.clear()
        food.title = "Voda"
        food.subtitle = "Flaširana voda"
        food.type = "bezalkoholno"
        food.price = 15.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/drink%2Fvoda.jpg?alt=media&token=27637c72-9e37-4bb0-80c2-6057165b4e61"
        food.extras.add(ExtraFood("Limun", 0f))
        food.extras.add(ExtraFood("Led", 0f))
        food.nutririonValues = NutritionValues("0", "0 g", "0 g", "2 mg", "0 mg", "0 g")
        database.reference.child("Drink").child("792b275f-9b77-4126-b430-c8462dc7acd7").child(UUID.randomUUID().toString()).setValue(food)
        food.extras.clear()
        food.title = "Kozel"
        food.subtitle = "Tamni"
        food.type = "alkoholno"
        food.price = 20.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/drink%2Fkozel_tamni.jpg?alt=media&token=c2b4bc63-fa20-4c23-996d-17c6acb10bd3"
        food.nutririonValues = NutritionValues("43.4", "0 g", "3.2 g", "0 mg", "21 mg", "0.2 g")
        database.reference.child("Drink").child("792b275f-9b77-4126-b430-c8462dc7acd7").child(UUID.randomUUID().toString()).setValue(food)
        food.extras.clear()
        food.title = "Kozel"
        food.subtitle = "Svijetli"
        food.type = "alkoholno"
        food.price = 20.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/drink%2Fkozel_svijetli.jpg?alt=media&token=b1532ab6-c86c-4f74-8291-086146b9d564"
        food.nutririonValues = NutritionValues("29", "0 g", "1.6 g", "4 mg", "21 mg", "0.2 g")
        database.reference.child("Drink").child("792b275f-9b77-4126-b430-c8462dc7acd7").child(UUID.randomUUID().toString()).setValue(food)
        food.extras.clear()
        food.title = "Kava"
        food.subtitle = "Crna"
        food.type = "toplo"
        food.price = 10.00f
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/drink%2Fkava.jpeg?alt=media&token=2b57ef7b-57e3-4926-8d8b-93d6e03e11e2"
        food.nutririonValues = NutritionValues("0", "0 g", "0 g", "2 mg", "49 mg", "0.1 g")
        database.reference.child("Drink").child("792b275f-9b77-4126-b430-c8462dc7acd7").child(UUID.randomUUID().toString()).setValue(food)
        food.extras.clear()
        food.title = "Čaj"
        food.subtitle = "Voćni"
        food.type = "toplo"
        food.price = 10.00f
        food.extras.add(ExtraFood("Limun", 0f))
        food.extras.add(ExtraFood("Med", 0f))
        food.imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-restoran.appspot.com/o/drink%2Fcaj.jpeg?alt=media&token=8e8dad54-fb24-4e12-8ea3-1277cd59374e"
        food.nutririonValues = NutritionValues("1", "0 g", "0.2 g", "4 mg", "18 mg", "0.1 g")
        database.reference.child("Drink").child("792b275f-9b77-4126-b430-c8462dc7acd7").child(UUID.randomUUID().toString()).setValue(food)
        food.extras.clear()
    }

    private fun emailLogin(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val isVerified = it.result?.user?.isEmailVerified ?: false
                    if (isVerified) {
                        Toast.makeText(this, getString(R.string.sign_in_message), Toast.LENGTH_SHORT).show()

                        database.reference.child("Users")
                            .orderByChild("email")
                            .equalTo(email).limitToFirst(1).addListenerForSingleValueEvent(object:
                                ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    sessionUser = p0.children.first().getValue(User::class.java) ?: User()

                                    val locale = Locale(sessionUser.language)
                                    Locale.setDefault(locale)
                                    val config = Configuration()
                                    config.locale = locale
                                    resources.updateConfiguration(config, resources.displayMetrics)

                                    for (valueRes in p0.children.first().child("orders").children) {
                                        val order = valueRes.getValue(Order::class.java)
                                        if(order != null) {
                                            sessionUser.orderHistory.add(order)
                                        }
                                    }

                                    for (valueRes in p0.children.first().child("address").children) {
                                        val order = valueRes.getValue(AddressModel::class.java)
                                        if(order != null) {
                                            sessionUser.addresses.add(order)
                                        }
                                    }

                                    userUid = p0.children.first().key ?: ""

                                    prefs.edit().putBoolean("isGuest", false).apply()
                                    startActivity(Intent(this@SplashActivity, MethodSelectActivity::class.java))
                                    finish()
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                    prefs.edit().putBoolean("isGuest", false).apply()
                                    startActivity(Intent(this@SplashActivity, MethodSelectActivity::class.java))
                                    finish()
                                }

                            })
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
