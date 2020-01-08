package hr.fer.grupa.erestoran.models

class Food(
    var id: String = "",
    var title: String = "",
    var subtitle: String = "",
    var price: Float = 0f,
    var imageUrl: String = "",
    var type: String = "",
    var isInCart: Boolean = false,
    var quantity: Int = 0
)