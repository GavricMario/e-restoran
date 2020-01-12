package hr.fer.grupa.erestoran.food

import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food

class ItemModel(var section: Int) : Section {

    var item : String = ""
    lateinit var food: Food
    lateinit var drink: Drink

    override fun isHeader(): Boolean {
        return false
    }

    override fun getName(): String {
        return item
    }

    override fun sectionPosition(): Int {
        return section
    }

    override fun getItem(): Food {
        return food
    }

    override fun getDrinkItem(): Drink {
        return drink
    }

}