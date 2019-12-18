package hr.fer.grupa.erestoran

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_zahvala.*
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RatingBar


class Zahvala : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zahvala)
        dostava_restoran.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                openDialog()
            }
        })

    }

    private fun openDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_rating)

        val c = dialog.findViewById(R.id.c) as Button
        val ratingBar = dialog.findViewById(R.id.ratingBar) as RatingBar

        c.setOnClickListener {
            if (ratingBar.getRating()!= 0f) {
                val intent = Intent(this, MethodSelectActivity::class.java)
                startActivity(intent)
            }
        }

        dialog.show()

    }

}
