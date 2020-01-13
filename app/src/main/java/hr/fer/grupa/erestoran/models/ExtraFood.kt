package hr.fer.grupa.erestoran.models

import java.io.Serializable

class ExtraFood(
    var title: String = "",
    var price: Float = 0f,
    var imageUrl: String = "",
    var selected: Boolean = false,
    var englishTitle: String = "",
    var germanTitle: String = ""
) : Serializable