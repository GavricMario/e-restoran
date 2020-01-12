package hr.fer.grupa.erestoran.account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.models.AddressModel
import kotlinx.android.synthetic.main.item_address.view.*


class AddressAdapter(
    private val context: Context,
    private val objects: ArrayList<AddressModel>
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {


    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentObj = objects[position % objects.size]

        val address = "${currentObj.streetName} ${currentObj.streetNumber}, ${currentObj.city} ${currentObj.postalCode}"
        holder.addressText.text = address

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addressText: TextView = view.addressText
    }
}
