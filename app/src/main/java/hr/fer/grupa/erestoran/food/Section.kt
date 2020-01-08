package hr.fer.grupa.erestoran.food

import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food

interface Section {

    fun isHeader(): Boolean
    fun getItem(): Food
    fun getDrinkItem(): Drink
    fun sectionPosition(): Int
    fun getName(): String

}