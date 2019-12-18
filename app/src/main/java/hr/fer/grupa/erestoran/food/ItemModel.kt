package hr.fer.grupa.erestoran.food

import hr.fer.grupa.erestoran.Food

class ItemModel(var section: Int) : Section {

    lateinit var item : String
    lateinit var food: Food

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

}