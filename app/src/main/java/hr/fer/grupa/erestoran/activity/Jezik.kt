package hr.fer.grupa.erestoran.activity

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.datasource.FirebaseDataSourceManager
import hr.fer.grupa.erestoran.util.sessionUser
import kotlinx.android.synthetic.main.activity_jezik.*
import java.util.*


class Jezik : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jezik)

        hrv.setOnClickListener{
            if (sessionUser.username != "") {
                FirebaseDataSourceManager.getInstance().setLanguage("hr")
            }

            val locale = Locale("hr")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)

            onBackPressed()
        }
        eng.setOnClickListener{
            if (sessionUser.username != "") {
                FirebaseDataSourceManager.getInstance().setLanguage("en")
            }

            val locale = Locale("en")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)

            onBackPressed()
        }
        de.setOnClickListener{
            if (sessionUser.username != "") {
                FirebaseDataSourceManager.getInstance().setLanguage("de")
            }

            val locale = Locale("de")
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)

            onBackPressed()
        }
    }

}

