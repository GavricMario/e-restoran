package hr.fer.grupa.erestoran.models

import java.io.Serializable

class NutritionValues(
    var calories: String = "0",
    var fat: String = "0g",
    var carbohydrates: String = "0g",
    var sodium: String = "0g",
    var potassium: String = "0g",
    var protein: String = "0g"
) : Serializable