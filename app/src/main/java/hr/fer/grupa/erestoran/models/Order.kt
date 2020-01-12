package hr.fer.grupa.erestoran.models

import java.io.Serializable

class Order(
    var restaurant: Restaurant = Restaurant(),
    var food: MutableList<Food> = mutableListOf(),
    var drink: MutableList<Drink> = mutableListOf(),
    var type: String = "",
    var orderTime: Long = 0
): Serializable