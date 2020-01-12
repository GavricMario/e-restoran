package hr.fer.grupa.erestoran.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.ExtraFood
import kotlinx.android.synthetic.main.item_food_extra.view.*

class FoodExtrasAdapter(val foodList: MutableList<ExtraFood>) :
    RecyclerView.Adapter<FoodExtrasAdapter.FoodExtraViewHolder>() {

    var onItemClick: ((ExtraFood, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodExtraViewHolder {
        return FoodExtraViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_food_extra,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodExtraViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    inner class FoodExtraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodTitle = itemView.food_title!!
        var foodExtraCheckBox = itemView.food_extra_checkbox!!

        init {
            foodExtraCheckBox.setOnClickListener {
                onItemClick?.invoke(foodList[adapterPosition], foodExtraCheckBox.isChecked)
            }
        }

        fun bind(food: ExtraFood) {
            foodTitle.text = food.title
            foodExtraCheckBox.isChecked = food.selected
        }
    }
}