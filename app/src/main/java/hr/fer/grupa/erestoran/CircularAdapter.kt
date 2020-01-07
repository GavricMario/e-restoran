package hr.fer.grupa.erestoran

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_menu_list.view.*


class CircularAdapter(
    private val context: Context,
    private val objects: Array<String>
) : RecyclerView.Adapter<CircularAdapter.CircularViewHolder>() {

    var firstVisiblePosition: Int = 0
    var lastVisiblePosition: Int = 0


    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {
        val currentObj = objects[position % objects.size]
        holder.menuItemText.text = currentObj

        if (position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2)) {
            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                (120 * context.resources.displayMetrics.density).toInt()
            )
            params.setMargins(135, 15, 135, 15)
            holder.itemView.layoutParams = params
        } else if (position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) - 1
                || position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) + 1) {
            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                (100 * context.resources.displayMetrics.density).toInt()
            )
            params.setMargins(90, 10, 90, 10)
            holder.itemView.layoutParams = params
        } else if (position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) - 2
                || position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) + 2) {
            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                (100 * context.resources.displayMetrics.density).toInt()
            )
            params.setMargins(45, 5, 45, 5)
            holder.itemView.layoutParams = params
        } else {
            val params = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                (80 * context.resources.displayMetrics.density).toInt()
            )
            params.setMargins(0, 0, 0, 0)
            holder.itemView.elevation = 0f
            holder.itemView.layoutParams = params
        }

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircularViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu_list, parent, false)
        return CircularViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    val MIDDLE: Int

    init {
        MIDDLE = HALF_MAX_VALUE - HALF_MAX_VALUE % objects.size
    }

    companion object {

        const val HALF_MAX_VALUE = Integer.MAX_VALUE / 2
    }

    class CircularViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuItemText = view.menuItemText
        val image = view.image
    }
}
