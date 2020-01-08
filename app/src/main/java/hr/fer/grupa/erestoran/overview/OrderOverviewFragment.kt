package hr.fer.grupa.erestoran.overview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.shuhart.stickyheader.StickyHeaderItemDecorator
import hr.fer.grupa.erestoran.MethodSelectActivity
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.databinding.OrderOverviewFragmentBinding
import hr.fer.grupa.erestoran.food.HeaderModel
import hr.fer.grupa.erestoran.food.ItemModel
import hr.fer.grupa.erestoran.models.Order

class OrderOverviewFragment : Fragment() {

    private lateinit var binding: OrderOverviewFragmentBinding

    private lateinit var adapter: OverviewAdapter

    private lateinit var order: Order

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.order_overview_fragment, container, false)
        binding.fragment = this
        val bundle = this.arguments
        if (bundle != null) {
            order = bundle.getSerializable("order") as Order
            showOrder(order)
        }
        return binding.root
    }

    private fun showOrder(order: Order) {
        binding.pickedRestaurant.text = order.restaurant.title
        initRecycler(order)
    }

    private fun initRecycler(order: Order) {
        val foodHeaderModel = HeaderModel(0)
        foodHeaderModel.header = "Food"
        order.allItems.add(foodHeaderModel)
        order.food.forEach { food ->
            val foodItem = ItemModel(0)
            foodItem.food = food
            order.allItems.add(foodItem)
        }
        val drinkHeaderModel = HeaderModel(order.food.size)
        drinkHeaderModel.header = "Drinks"
        order.allItems.add(drinkHeaderModel)
        order.drink.forEach { drink ->
            val drinkItem = ItemModel(order.food.size)
            drinkItem.drink = drink
            order.allItems.add(drinkItem)
        }
        adapter = OverviewAdapter(requireContext(), order.allItems)
        val decorator = StickyHeaderItemDecorator(adapter)
        decorator.attachToRecyclerView(binding.recyclerView)
        adapter.minusClick = { item, position ->
            if (order.allItems[position].sectionPosition() == 0) {
                if (order.allItems[position].getItem().quantity > 1)
                    order.allItems[position].getItem().quantity--
            } else {
                if (order.allItems[position].getDrinkItem().quantity > 1)
                    order.allItems[position].getDrinkItem().quantity--
            }
            adapter.notifyItemChanged(position)
            updateTotalPrice(order)
        }
        adapter.plusClick = { item, position ->
            if (order.allItems[position].sectionPosition() == 0) {
                order.allItems[position].getItem().quantity++
            } else {
                order.allItems[position].getDrinkItem().quantity++
            }
            adapter.notifyItemChanged(position)
            updateTotalPrice(order)
        }
        adapter.addToCartClick = { item, position ->
            order.allItems.removeAt(position)
            adapter.notifyItemRemoved(position)
            updateTotalPrice(order)
        }
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        updateTotalPrice(order)
    }

    private fun updateTotalPrice(order: Order) {
        var totalPrice = 0f
        order.allItems.forEach {
            if (!it.isHeader()) {
                totalPrice += if (it.sectionPosition() == 0) {
                    it.getItem().price * it.getItem().quantity
                } else {
                    it.getDrinkItem().price * it.getDrinkItem().quantity
                }
            }
        }
        binding.totalPrice.text = totalPrice.toString()
    }

    override fun onPause() {
        super.onPause()
        order.allItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun placeOrder() {
        if (order.type == "restaurant") {
            Toast.makeText(
                requireContext(),
                "Your order has been placed. Thank You for using our application!",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(requireContext(), MethodSelectActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        } else if (order.type == "delivery") {
            //todo start payment activity
        }
    }
}