package hr.fer.grupa.erestoran.overview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.shuhart.stickyheader.StickyAdapter
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.food.Section
import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food.view.add_to_cart
import kotlinx.android.synthetic.main.item_food.view.description
import kotlinx.android.synthetic.main.item_food.view.food_image
import kotlinx.android.synthetic.main.item_food.view.name
import kotlinx.android.synthetic.main.item_food.view.price
import kotlinx.android.synthetic.main.item_food_header.view.*
import kotlinx.android.synthetic.main.item_overview.view.*

class OverviewAdapter(var context: Context, var sections: MutableList<Section>) :
    StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>()  {


    private val LAYOUT_HEADER = 0
    private val LAYOUT_ITEM = 1

    var onItemClick: ((Section) -> Unit)? = null
    var addToCartClick: ((Section, Int) -> Unit)? = null
    var minusClick: ((Section, Int) -> Unit)? = null
    var plusClick: ((Section, Int) -> Unit)? = null

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return createViewHolder(parent!!, LAYOUT_HEADER)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (p1 == LAYOUT_HEADER) {
            HeaderViewHolder(inflater.inflate(R.layout.item_food_header, p0, false))
        } else {
            FoodViewHolder(inflater.inflate(R.layout.item_overview, p0, false))
        }
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, headerPosition: Int) {
        (holder as HeaderViewHolder).headerType.text = sections[headerPosition].getName()
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (sections[p1].isHeader()) {
            (p0 as HeaderViewHolder).headerType.text = sections[p1].getName()
        } else {
            val holder = p0 as FoodViewHolder
            if (sections[p1].sectionPosition() == 0) {
                val food = sections[p1].getItem()
                holder.bindFood(food)
            } else {
                val drink = sections[p1].getDrinkItem()
                holder.bindDrink(drink)
            }
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return sections[itemPosition].sectionPosition()
    }

    override fun getItemViewType(position: Int): Int {
        return if (sections[position].isHeader()) {
            LAYOUT_HEADER
        } else {
            LAYOUT_ITEM
        }
    }


    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodImage = itemView.food_image
        var name = itemView.name!!
        var description = itemView.description!!
        var price = itemView.price!!
        var addToCartView = itemView.add_to_cart!!
        var quantity = itemView.quantity!!

        init {
            itemView.findViewById<LinearLayout>(R.id.root_item_view).setOnClickListener {
                onItemClick?.invoke(sections[adapterPosition])
            }
            itemView.findViewById<ImageView>(R.id.add_to_cart).setOnClickListener {
                addToCartClick?.invoke(sections[adapterPosition], adapterPosition)
                itemView.findViewById<SwipeLayout>(R.id.swipe_layout).close()
            }
            itemView.findViewById<TextView>(R.id.minus).setOnClickListener {
                minusClick?.invoke(sections[adapterPosition], adapterPosition)
            }
            itemView.findViewById<TextView>(R.id.plus).setOnClickListener {
                plusClick?.invoke(sections[adapterPosition], adapterPosition)
            }
        }

        fun bindFood(food: Food) {
            name.text = food.title
            description.text = food.subtitle
            price.text = food.price.toString()
            quantity.text = food.quantity.toString()
            if (food.isInCart) {
                addToCartView.setBackgroundColor(Color.RED)
            } else {
                addToCartView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
            }
        }

        fun bindDrink(drink: Drink) {
            name.text = drink.title
            description.text = drink.subtitle
            price.text = drink.price.toString()
            quantity.text = drink.quantity.toString()
            if (drink.isInCart) {
                addToCartView.setBackgroundColor(Color.RED)
            } else {
                addToCartView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
            }
        }

    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerType = itemView.headerType!!
    }

}