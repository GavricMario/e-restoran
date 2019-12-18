package hr.fer.grupa.erestoran.restaurants

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.Restaurant
import kotlinx.android.synthetic.main.item_restaurant.view.*
import kotlin.math.roundToInt

class RestaurantAdapter(
    private var items: MutableList<Restaurant>,
    val context: Context,
    private val clickListener: (Restaurant, Int) -> Unit
) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_restaurant,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(items[position], position, clickListener)
    }

    inner class RestaurantViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(restaurant: Restaurant, position: Int, clickListener: (Restaurant, Int) -> Unit) {
            view.restaurant_title.text = restaurant.title
            view.restaurant_subtitle.text = restaurant.address
            view.distance.text = "${restaurant.distance}km"
            if (restaurant.isSelected)
                view.rootView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorRestaurantSelected
                    )
                )
            else
                view.rootView.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorDefault
                    )
                )
            view.setOnClickListener { clickListener(restaurant, position) }
            //        Picasso.get().load(items[position].imageUrl).into(holder.image)
        }
    }

    fun setItems(newItems: MutableList<Restaurant>) {
        this.items = newItems
        this.notifyDataSetChanged()
    }

}