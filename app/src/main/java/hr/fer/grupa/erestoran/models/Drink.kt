package hr.fer.grupa.erestoran.models

import java.io.Serializable

class Drink(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var price: Float = 0f,
    var imageUrl: String = "",
    var type: String = "",
    var isInCart: Boolean = false,
    var quantity: Int = 1,
    var extras: MutableList<ExtraFood> = mutableListOf(),
    var drinkSize: String = "Medium 0.5L",
    var nutririonValues: NutritionValues = NutritionValues(),
    var englishTitle: String = "",
    var germanTitle: String = "",
    var englishSubtitle: String = "",
    var germanSubtitle: String = ""
): Serializable