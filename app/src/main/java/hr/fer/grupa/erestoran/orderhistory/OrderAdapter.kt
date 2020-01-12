package hr.fer.grupa.erestoran.orderhistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.Order
import hr.fer.grupa.erestoran.order.OrderBaseActivity
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
    }
}