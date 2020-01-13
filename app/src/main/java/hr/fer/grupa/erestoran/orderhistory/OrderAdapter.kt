package hr.fer.grupa.erestoran.orderhistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.Order
import hr.fer.grupa.erestoran.order.OrderBaseActivity
import hr.fer.grupa.erestoran.util.sessionUser
import kotlinx.android.synthetic.main.item_order.view.*
import java.util.*
import kotlin.collections.ArrayList
import java.text.SimpleDateFormat


class OrderAdapter(
    private val context: Context,
    private var objects: ArrayList<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentObj = objects[position % objects.size]

        holder.restaurant.text = currentObj.restaurant.title
        holder.type.text = currentObj.type

        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentObj.orderTime

        holder.date.text = formatter.format(calendar.time)

        holder.orderAgain.setOnClickListener {
            context.startActivity(Intent(context, OrderBaseActivity::class.java)
                .putExtra("order", currentObj)
                .putExtra("type", currentObj.type)
            )
        }
        holder.foods.text = ""
        for (food in currentObj.food) {
            val text = holder.foods.text
            if (sessionUser.language == "hr") {
                if (holder.foods.text == "") {
                    holder.foods.text = "- ${food.title} x ${food.quantity}"
                } else {
                    holder.foods.text = "$text, ${food.title} x ${food.quantity}"
                }
            } else if (sessionUser.language == "de") {
                if (holder.foods.text == "") {
                    holder.foods.text = "- ${food.germanTitle} x ${food.quantity}"
                } else {
                    holder.foods.text = "$text, ${food.germanTitle} x ${food.quantity}"
                }
            } else {
                if (holder.foods.text == "") {
                    holder.foods.text = "- ${food.englishTitle} x ${food.quantity}"
                } else {
                    holder.foods.text = "$text, ${food.englishTitle} x ${food.quantity}"
                }
            }
        }

        holder.drinks.text = ""
        for (drink in currentObj.drink) {
            val text = holder.drinks.text
            if (sessionUser.language == "hr") {
                if (holder.drinks.text == "") {
                    holder.drinks.text = "- ${drink.title} x ${drink.quantity}"
                } else {
                    holder.drinks.text = "$text, ${drink.title} x ${drink.quantity}"
                }
            } else if (sessionUser.language == "de") {
                if (holder.drinks.text == "") {
                    holder.drinks.text = "- ${drink.germanTitle} x ${drink.quantity}"
                } else {
                    holder.drinks.text = "$text, ${drink.germanTitle} x ${drink.quantity}"
                }
            } else {
                if (holder.drinks.text == "") {
                    holder.drinks.text = "- ${drink.englishTitle} x ${drink.quantity}"
                } else {
                    holder.drinks.text = "$text, ${drink.englishTitle} x ${drink.quantity}"
                }
            }
        }

        holder.layout.setOnClickListener {
            if (holder.foods.visibility == View.VISIBLE) {
                holder.foods.visibility = View.GONE
                holder.drinks.visibility = View.GONE
            } else {
                holder.foods.visibility = View.VISIBLE
                holder.drinks.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    fun setItems(orders: ArrayList<Order>) {
        objects = orders
        notifyDataSetChanged()
    }

    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurant: TextView = view.restaurant
        val date: TextView = view.date
        val type: TextView = view.type
        val orderAgain: Button = view.orderAgainButton

        val layout: ConstraintLayout = view.layout
        val foods: TextView = view.foods
        val drinks: TextView = view.drinks
    }
}