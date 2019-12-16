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

class RestaurantAdapter(private val items: MutableList<Restaurant>, val context: Context) :
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.subtitle.text = items[position].address
        holder.distance.text = "${items[position].distance}km"
        if (items[position].isSelected)
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorRestaurantSelected))
        else
            holder.rootView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorDefault))
//        Picasso.get().load(items[position].imageUrl).into(holder.image)
    }

    inner class RestaurantViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.restaurant_title
        val subtitle: TextView = view.restaurant_subtitle
        val image: ImageView = view.restaurant_image
        val distance: TextView = view.distance
        val rootView: LinearLayout = view.root_view
    }

}