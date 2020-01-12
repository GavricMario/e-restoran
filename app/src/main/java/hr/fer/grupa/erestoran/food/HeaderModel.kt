package hr.fer.grupa.erestoran.food

import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food

class HeaderModel(private var section: Int) : Section {

    override fun getItem(): Food {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDrinkItem(): Drink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    lateinit var header : String

    override fun isHeader(): Boolean {
        return true
    }

    override fun getName(): String {
        return header
    }

    override fun sectionPosition(): Int {
        return section
    }
}