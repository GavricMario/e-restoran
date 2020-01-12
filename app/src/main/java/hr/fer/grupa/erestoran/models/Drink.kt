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
    var drinkSize: String = "Medium 0.5L"
): Serializable