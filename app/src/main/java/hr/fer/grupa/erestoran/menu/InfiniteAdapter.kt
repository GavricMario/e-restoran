package hr.fer.grupa.erestoran.menu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularimageview.CircularImageView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.account.AccountDetailsActivity
import hr.fer.grupa.erestoran.account.AddAddressActivity
import hr.fer.grupa.erestoran.activity.*
import hr.fer.grupa.erestoran.models.User
import hr.fer.grupa.erestoran.orderhistory.OrderHistoryActivity
import hr.fer.grupa.erestoran.util.sessionUser
import hr.fer.grupa.erestoran.util.userUid
import kotlinx.android.synthetic.main.item_menu_list.view.*


class InfiniteAdapter(
    private val context: Context,
    private val activityContext: Context,
    private val objects: Array<String>
) : RecyclerView.Adapter<InfiniteAdapter.CircularViewHolder>() {

    var firstVisiblePosition: Int = 0
    var lastVisiblePosition: Int = 0


    override fun onBindViewHolder(holder: CircularViewHolder, position: Int) {
        val currentObj = objects[position % objects.size]

        when(currentObj) {
            "Change Language" -> holder.menuItemText.text = context.getString(R.string.change_language_setting)
            "Add Address" -> holder.menuItemText.text = context.getString(R.string.add_address_setting)
            "Tutorial" -> holder.menuItemText.text = context.getString(R.string.tutorial_setting)
            "Profile" -> holder.menuItemText.text = context.getString(R.string.profile_setting)
            "Order History" -> holder.menuItemText.text = context.getString(R.string.order_history_setting)
            "Log Out" -> holder.menuItemText.text = context.getString(R.string.log_out_setting)
        }

        when(currentObj) {
            "Change Language" -> holder.image.setImageResource(R.drawable.language)
            "Add Address" -> holder.image.setImageResource(R.drawable.address)
            "Tutorial" -> holder.image.setImageResource(R.drawable.tutorial)
            "Profile" -> holder.image.setImageResource(R.drawable.user_login)
            "Order History" -> holder.image.setImageResource(R.drawable.order_history)
            "Log Out" -> holder.image.setImageResource(R.drawable.user_anonim)
        }

        when(currentObj) {
            "Change Language" -> holder.container.setOnClickListener {
                val intent = Intent(context, Jezik::class.java)
                context.startActivity(intent)
                try {
                    (activityContext as MethodSelectActivity).finishActivity()
                } catch (e: Exception) {
                    //nothing
                }
            }
            "Add Address" -> holder.container.setOnClickListener {
                val intent = Intent(context, AddAddressActivity::class.java)
                context.startActivity(intent)
            }
            "Tutorial" -> holder.container.setOnClickListener {
                val intent = Intent(context, Tutorial1::class.java)
                context.startActivity(intent)
            }
            "Profile" -> holder.container.setOnClickListener {
                val intent = Intent(context, AccountDetailsActivity::class.java)
                context.startActivity(intent)
            }
            "Order History" -> holder.container.setOnClickListener {
                val intent = Intent(context, OrderHistoryActivity::class.java)
                context.startActivity(intent)
            }
            "Log Out" -> holder.container.setOnClickListener {
                val prefs = context.getSharedPreferences(context.resources.getString(R.string.app_name), AppCompatActivity.MODE_PRIVATE)
                prefs.edit()
                    .remove("email")
                    .remove("password")
                    .apply()
                sessionUser = User()
                userUid = ""
                val intent = Intent(context, UserTypeSelectActivity::class.java)
                context.startActivity(intent)
                try {
                    (activityContext as MethodSelectActivity).finishActivity()
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
        val menuItemText: TextView = view.menuItemText
        val image: CircularImageView = view.image
        val container: LinearLayout = view.container
    }
}
