package hr.fer.grupa.erestoran.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.datasource.FirebaseDataSourceManager
import hr.fer.grupa.erestoran.util.sessionUser
import kotlinx.android.synthetic.main.activity_jezik.*

class Jezik : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jezik)

        val prefs = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)

        hrv.setOnClickListener{
            if (sessionUser.username != "") {
                FirebaseDataSourceManager.getInstance().setLanguage("hr")
            }
            prefs.edit().putString("language", "hr").apply()
            onBackPressed()
        }
        eng.setOnClickListener{
            if (sessionUser.username != "") {
                FirebaseDataSourceManager.getInstance().setLanguage("en")
            }
            prefs.edit().putString("firstUse", "en").apply()
            onBackPressed()
        }
        de.setOnClickListener{
            if (sessionUser.username != "") {
                FirebaseDataSourceManager.getInstance().setLanguage("de")
            }
            prefs.edit().putString("firstUse", "de").apply()
            onBackPressed()
        }
    }

}

