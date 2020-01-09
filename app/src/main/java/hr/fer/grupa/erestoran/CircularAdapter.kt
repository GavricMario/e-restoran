package hr.fer.grupa.erestoran

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

        when(currentObj) {
            "Change Language" -> holder.image.setImageResource(R.drawable.language)
            "Add Address" -> holder.image.setImageResource(R.drawable.address)
            "Tutorial" -> holder.image.setImageResource(R.drawable.tutorial)
            "MoreSettings3" -> holder.image.setImageResource(R.drawable.user_anonim)
            "Profile" -> holder.image.setImageResource(R.drawable.user_login)
            "Order History" -> holder.image.setImageResource(R.drawable.order_history)
            "Log Out" -> holder.image.setImageResource(R.drawable.user_anonim)
        }

        when(currentObj) {
            "Change Language" -> holder.container.setOnClickListener {
                val intent = Intent(context, Jezik::class.java)
                context.startActivity(intent)
            }
            "Add Address" -> holder.container.setOnClickListener {
                //TODO add address view
            }
            "Tutorial" -> holder.container.setOnClickListener {
                val intent = Intent(context, Tutorial1::class.java)
                context.startActivity(intent)
            }
            "MoreSettings3" -> holder.container.setOnClickListener {
                //TODO add more setting
            }
            "Profile" -> holder.container.setOnClickListener {
                //TODO profile edit view
            }
            "Order History" -> holder.container.setOnClickListener {
                //TODO history view
            }
            "Log Out" -> holder.container.setOnClickListener {
                val prefs = context.getSharedPreferences(context.resources.getString(R.string.app_name), AppCompatActivity.MODE_PRIVATE)
                prefs.edit()
                    .remove("email")
                    .remove("password")
                    .apply()
                sessionUser = User()
                val intent = Intent(context, UserTypeSelectActivity::class.java)
                context.startActivity(intent)
                try {
                    (context as MethodSelectActivity).finishActivity()
                } catch (e: Exception) {
                    //nothing
                }
            }
        }

//        if (position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2)) {
//            val params = RecyclerView.LayoutParams(
//                RecyclerView.LayoutParams.MATCH_PARENT,
//                (120 * context.resources.displayMetrics.density).toInt()
//            )
//            params.setMargins(135, 15, 135, 15)
//            holder.itemView.layoutParams = params
//        } else if (position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) - 1
//                || position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) + 1) {
//            val params = RecyclerView.LayoutParams(
//                RecyclerView.LayoutParams.MATCH_PARENT,
//                (100 * context.resources.displayMetrics.density).toInt()
//            )
//            params.setMargins(90, 10, 90, 10)
//            holder.itemView.layoutParams = params
//        } else if (position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) - 2
//                || position == lastVisiblePosition - ((lastVisiblePosition - firstVisiblePosition)/2) + 2) {
//            val params = RecyclerView.LayoutParams(
//                RecyclerView.LayoutParams.MATCH_PARENT,
//                (100 * context.resources.displayMetrics.density).toInt()
//            )
//            params.setMargins(45, 5, 45, 5)
//            holder.itemView.layoutParams = params
//        } else {
//            val params = RecyclerView.LayoutParams(
//                RecyclerView.LayoutParams.MATCH_PARENT,
//                (80 * context.resources.displayMetrics.density).toInt()
//            )
//            params.setMargins(0, 0, 0, 0)
//            holder.itemView.elevation = 0f
//            holder.itemView.layoutParams = params
//        }

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
        val container = view.container
    }
}
