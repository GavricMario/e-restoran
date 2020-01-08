package hr.fer.grupa.erestoran.models

import java.io.Serializable

class Restaurant(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var imageUrl: String = "",
    var distance: Float = 0f,
    var address: String = "",
    var isSelected: Boolean = false
): Serializable