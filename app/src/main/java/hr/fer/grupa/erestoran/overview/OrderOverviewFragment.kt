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
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.activity.Payment
import hr.fer.grupa.erestoran.activity.Zahvala
import hr.fer.grupa.erestoran.databinding.OrderOverviewFragmentBinding
import hr.fer.grupa.erestoran.datasource.FirebaseDataSourceManager
import hr.fer.grupa.erestoran.food.HeaderModel
import hr.fer.grupa.erestoran.food.ItemModel
import hr.fer.grupa.erestoran.food.Section
import hr.fer.grupa.erestoran.models.Order

class OrderOverviewFragment : Fragment() {

    private lateinit var binding: OrderOverviewFragmentBinding

    private lateinit var adapter: OverviewAdapter

    private lateinit var order: Order

    private val allItems = mutableListOf<Section>()

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
        foodHeaderModel.header = getString(R.string.food)
        allItems.add(foodHeaderModel)
        order.food.forEach { food ->
            val foodItem = ItemModel(0)
            foodItem.food = food
            allItems.add(foodItem)
        }
        val drinkHeaderModel = HeaderModel(order.food.size)
        drinkHeaderModel.header = getString(R.string.drinks)
        allItems.add(drinkHeaderModel)
        order.drink.forEach { drink ->
            val drinkItem = ItemModel(order.food.size)
            drinkItem.drink = drink
            allItems.add(drinkItem)
        }
        adapter = OverviewAdapter(requireContext(), allItems)
        val decorator = StickyHeaderItemDecorator(adapter)
        decorator.attachToRecyclerView(binding.recyclerView)
        adapter.minusClick = { item, position ->
            if (allItems[position].sectionPosition() == 0) {
                if (allItems[position].getItem().quantity > 1) {
                    allItems[position].getItem().quantity--
                }
            } else {
                if (allItems[position].getDrinkItem().quantity > 1) {
                    allItems[position].getDrinkItem().quantity--
                }
            }
            adapter.notifyItemChanged(position)
            updateTotalPrice(order)
        }
        adapter.plusClick = { item, position ->
            if (allItems[position].sectionPosition() == 0) {
                allItems[position].getItem().quantity++
            } else {
                allItems[position].getDrinkItem().quantity++
            }
            adapter.notifyItemChanged(position)
            updateTotalPrice(order)
        }
        adapter.addToCartClick = { item, position ->
            allItems.removeAt(position)
            if (allItems[position].sectionPosition() == 0) {
                order.food.remove(allItems[position].getItem())
            } else {
                order.drink.remove(allItems[position].getDrinkItem())
            }
            adapter.notifyItemRemoved(position)
            updateTotalPrice(order)
        }
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        updateTotalPrice(order)
    }

    private fun updateTotalPrice(order: Order) {
        var totalPrice = 0f
        allItems.forEach {
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
        allItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun placeOrder() {
        order.orderTime = System.currentTimeMillis()
        FirebaseDataSourceManager.getInstance().saveOrder(order)
        if (order.type == "restaurant") {
            Toast.makeText(
                requireContext(),
                "Your order has been placed. Thank You for using our application!",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(requireContext(), Zahvala::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        } else if (order.type == "delivery") {
            val intent = Intent(requireContext(), Payment::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}