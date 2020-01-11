package hr.fer.grupa.erestoran.models

import java.io.Serializable

class Order(
    var restaurant: Restaurant,
    var food: MutableList<Food>,
    var drink: MutableList<Drink>,
    var type: String,
    var orderTime: Long
): Serializable