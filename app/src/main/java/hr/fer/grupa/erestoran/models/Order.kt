package hr.fer.grupa.erestoran.models

import hr.fer.grupa.erestoran.food.Section
import java.io.Serializable

class Order(
    var restaurant: Restaurant,
    var food: MutableSet<Food>,
    var drink: MutableSet<Drink>,
    var allItems: MutableList<Section>
): Serializable