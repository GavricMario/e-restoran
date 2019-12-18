package hr.fer.grupa.erestoran.food

import hr.fer.grupa.erestoran.Food

interface Section {

    fun isHeader(): Boolean
    fun getItem(): Food
    fun sectionPosition(): Int
    fun getName(): String

}