package hr.fer.grupa.erestoran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import hr.fer.grupa.erestoran.order.OrderBaseActivity
import hr.fer.grupa.erestoran.restaurants.RestaurantsFragment


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val extra = intent.extras
        val type = extra?.getString("type", "")

    }
}
