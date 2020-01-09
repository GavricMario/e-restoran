package hr.fer.grupa.erestoran.food

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SwipeLayout
import com.shuhart.stickyheader.StickyAdapter
import hr.fer.grupa.erestoran.Food
import hr.fer.grupa.erestoran.R
import kotlinx.android.synthetic.main.item_food.view.*
import kotlinx.android.synthetic.main.item_food_header.view.*

class FoodAdapter(var context: Context, var sections: MutableList<Section>) :
    StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>() {

    private val LAYOUT_HEADER = 0
    private val LAYOUT_ITEM = 1

    var onItemClick: ((Section) -> Unit)? = null
    var addToCartClick: ((Section) -> Unit)? = null

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
            val food = sections[p1].getItem()
            holder.bind(food)
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
        var isInCartView = itemView.is_in_cart_view!!

        init {
            itemView.findViewById<LinearLayout>(R.id.root_item_view).setOnClickListener {
                onItemClick?.invoke(sections[adapterPosition])
            }
            itemView.findViewById<ImageView>(R.id.add_to_cart).setOnClickListener {
                addToCartClick?.invoke(sections[adapterPosition])
            }
        }

        fun bind(food: Food) {
            name.text = food.title
            description.text = food.subtitle
            price.text = food.price.toString()
            if (food.isInCart)
                isInCartView.visibility = View.VISIBLE
            else
                isInCartView.visibility = View.INVISIBLE
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var headerType = itemView.headerType!!
    }
}