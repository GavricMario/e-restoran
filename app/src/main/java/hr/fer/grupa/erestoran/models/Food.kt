package hr.fer.grupa.erestoran.models

import java.io.Serializable

class Food(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var price: Float = 0f,
    var imageUrl: String = "",
    var type: String = "",
    var isInCart: Boolean = false,
    var quantity: Int = 1,
    var extras: MutableList<ExtraFood> = mutableListOf(),
    var rareness: Int = 0
): Serializable