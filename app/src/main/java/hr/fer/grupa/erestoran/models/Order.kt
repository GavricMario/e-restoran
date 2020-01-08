package hr.fer.grupa.erestoran.models

class Order(
    var restaurant: Restaurant,
    var food: MutableSet<Food>,
    var drink: MutableSet<Drink>
)