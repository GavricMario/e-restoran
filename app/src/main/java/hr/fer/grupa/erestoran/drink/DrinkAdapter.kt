package hr.fer.grupa.erestoran.drink

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.shuhart.stickyheader.StickyAdapter
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.food.Section
import hr.fer.grupa.erestoran.models.Drink
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food_header.view.*

class DrinkAdapter(var context: Context, var sections: MutableList<Section>) :
    StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>() {

    private val LAYOUT_HEADER = 0
    private val LAYOUT_ITEM = 1

    var onItemClick: ((Section, Int) -> Unit)? = null
    var addToCartClick: ((Section, Int) -> Unit)? = null

    override fun onCreateHeaderViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        return createViewHolder(parent!!, LAYOUT_HEADER)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return if (p1 == LAYOUT_HEADER) {
            HeaderViewHolder(inflater.inflate(R.layout.item_food_header, p0, false))
        } else {
            FoodViewHolder(inflater.inflate(R.layout.item_food, p0, false))
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
            val drink = sections[p1].getDrinkItem()
            holder.bind(drink)
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
        var drinkImage = itemView.food_image
        var name = itemView.name!!
        var description = itemView.description!!
        var price = itemView.price!!
        var isInCartView = itemView.is_in_cart_view!!
        var addToCartView = itemView.add_to_cart!!

        init {
            itemView.findViewById<LinearLayout>(R.id.root_item_view).setOnClickListener {
                onItemClick?.invoke(sections[adapterPosition], adapterPosition)
            }
            itemView.findViewById<ImageView>(R.id.add_to_cart).setOnClickListener {
                addToCartClick?.invoke(sections[adapterPosition], adapterPosition)
                itemView.findViewById<SwipeLayout>(R.id.swipe_layout).close()
            }
        }

        fun bind(drink: Drink) {
            name.text = drink.title
            description.text = drink.subtitle
            price.text = drink.price.toString()
            if (drink.isInCart) {
                isInCartView.visibility = View.VISIBLE
                addToCartView.setBackgroundColor(Color.RED)
            } else {
                isInCartView.visibility = View.INVISIBLE
                addToCartView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen))
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerType = itemView.headerType!!
    }
}