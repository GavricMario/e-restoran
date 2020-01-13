package hr.fer.grupa.erestoran.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.NutritionValues
import kotlinx.android.synthetic.main.activity_nutrition.*

class NutritionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        val nutrition: NutritionValues = intent.getSerializableExtra("nutrition")!! as NutritionValues
        val name: String = intent.getStringExtra("name")!!
        val image: String = intent.getStringExtra("imageUrl")!!

        itemName.text = name

        Picasso.get().load(image).into(imageOfItem)

        caloriesText.text = nutrition.calories
        fatText.text = nutrition.fat
        carbohydratesText.text = nutrition.carbohydrates
        sodiumText.text = nutrition.sodium
        potassiumText.text = nutrition.potassium
        proteinText.text = nutrition.protein

    }
}
