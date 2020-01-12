package hr.fer.grupa.erestoran.activity

import android.content.Intent
import android.media.Rating
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.datasource.FirebaseDataSourceManager
import hr.fer.grupa.erestoran.models.RatingModel
import hr.fer.grupa.erestoran.util.sessionUser
import kotlinx.android.synthetic.main.activity_zahvala.*


class Zahvala : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zahvala)

        initListeners()
    }

    private fun initListeners() {

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        if (prefs.getBoolean("isGuest", false)) {
            ratingBar.visibility = View.INVISIBLE
            commentText.visibility = View.INVISIBLE
            ocjena.visibility = View.INVISIBLE
        }

        ocjena.setOnClickListener{
            if (ratingBar.rating != 0f) {
                Toast.makeText(this,  getString(R.string.back_input_message), Toast.LENGTH_SHORT)
                    .show()
                FirebaseDataSourceManager.getInstance().saveRating(RatingModel(sessionUser.username, ratingBar.rating, commentText.text.trim().toString()))
                dostava_restoran.performClick()
            } else {
                Toast.makeText(this,  getString(R.string.rateus_string), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        dostava_restoran.setOnClickListener {
            if (ratingBar.rating != 0f) {
                if (prefs.getBoolean("isGuest", false)) {
                    val intent = Intent(this, UserTypeSelectActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MethodSelectActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }



    }

}
