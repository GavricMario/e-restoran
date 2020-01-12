package hr.fer.grupa.erestoran.account

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.util.checkCreditCard
import hr.fer.grupa.erestoran.util.isValidUsername
import hr.fer.grupa.erestoran.models.User
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var cardValidated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initFirebase()
        initListeners()

    }

    private fun initFirebase() {
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    private fun initListeners() {
        credit_card_text.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val str = credit_card_text.text.toString().trim()
                if(!checkCreditCard(str)) {
                    credit_card_text.error = getString(R.string.enter_valid_number)
                    credit_card_text.requestFocus()
                }else{
                    cardValidated = true
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

        })

        reg_btn2.setOnClickListener {
            database.reference.child("Usernames").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    val name = nameText.text.toString().trim()
                    val surname = surnameText.text.toString().trim()
                    val email = emailText.text.toString().trim()
                    val password = passwordText.text.toString().trim()
                    val confirmPassword = passwordConfirmText.text.toString().trim()
                    val username = usernameText.text.toString().trim()
                    val creditCard = credit_card_text.text.toString().trim()

                    if(name.isEmpty()){
                        nameText.error = getString(R.string.enter_first_name)
                        nameText.requestFocus()
                    } else if(surname.isEmpty()){
                        surnameText.error = getString(R.string.enter_last_name)
                        surnameText.requestFocus()
                    } else if(username.isEmpty()){
                        usernameText.error = getString(R.string.enter_username)
                        usernameText.requestFocus()
                    } else if (usernameText.length() < 6 || usernameText.length() > 30 ) {
                        usernameText.error = getString(R.string.username_must_be_error)
                        usernameText.requestFocus()
                    } else if(!isValidUsername(
                            username
                        )
                    ){
                        usernameText.error = getString(R.string.special_characters_error)
                        usernameText.requestFocus()
                    } else if(p0.hasChild(usernameText.text.toString().trim())){
                        usernameText.error = getString(R.string.username_taken_error)
                        usernameText.requestFocus()
                    } else if(email.isEmpty()){
                        emailText.error = getString(R.string.enter_email)
                        emailText.requestFocus()
                    } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailText.error = getString(R.string.enter_valid_email)
                        emailText.requestFocus()
                    } else if(password.isEmpty()){
                        passwordText.error = getString(R.string.enter_password)
                        passwordText.requestFocus()
                    } else if(password.length < 6){
                        passwordText.error = getString(R.string.password_length_error)
                        passwordText.requestFocus()
                    } else if (password != confirmPassword) {
                        passwordConfirmText.error = getString(R.string.password_match_error)
                        passwordConfirmText.requestFocus()
                    } else if(creditCard.isEmpty()){
                        credit_card_text.error=getString(R.string.enter_credit_card)
                        credit_card_text.requestFocus()
                    } else if(!cardValidated){
                        credit_card_text.error=getString(R.string.validate_credit_card)
                        credit_card_text.requestFocus()
                    } else {
                        val user = User(
                            username = username,
                            firstName = name,
                            lastName = surname,
                            email = email,
                            creditCard = creditCard
                        )
                        registerUser(user, password)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    //DO NOTHING
                }
            })
        }
    }

    private fun registerUser(user: User, password: String) {
        progressBar2.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val userId = auth.currentUser?.uid

                if (userId != null) {
                    FirebaseDatabase.getInstance().getReference("Usernames").child(user.username).setValue(userId)
                    FirebaseDatabase.getInstance().getReference("Users").child(userId).setValue(user).addOnCompleteListener {
                        Toast.makeText(baseContext, getString(R.string.registration_success), Toast.LENGTH_LONG).show()
                    }
                    auth.currentUser?.sendEmailVerification()
                } else {
                    Toast.makeText(baseContext, getString(R.string.registration_fail), Toast.LENGTH_SHORT).show()
                }

                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            } else {
                try {
                    throw it.exception ?: Exception()
                } catch (weakPassword: FirebaseAuthWeakPasswordException) {
                    progressBar2.visibility = View.GONE
                    Toast.makeText(baseContext, getString(R.string.stronger_password_error), Toast.LENGTH_LONG).show()
                    passwordText.requestFocus()
                } catch (malformedEmail: FirebaseAuthInvalidCredentialsException) {
                    progressBar2.visibility = View.GONE
                    Toast.makeText(baseContext, getString(R.string.email_malformed_error), Toast.LENGTH_LONG).show()
                    emailText.requestFocus()
                } catch (existEmail: FirebaseAuthUserCollisionException) {
                    progressBar2.visibility = View.GONE
                    Toast.makeText(baseContext, getString(R.string.email_in_use_error), Toast.LENGTH_LONG).show()
                    emailText.requestFocus()
                } catch (e: Exception) {
                    progressBar2.visibility = View.GONE
                    Toast.makeText(baseContext, getString(R.string.registration_fail), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}
