package hr.fer.grupa.erestoran.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shuhart.stickyheader.StickyHeaderItemDecorator
import hr.fer.grupa.erestoran.models.Food
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    private lateinit var binding: FragmentFoodBinding

    private val database = FirebaseDatabase.getInstance().reference

    private val starters = mutableListOf<Food>()
    private val mains = mutableListOf<Food>()
    private val deserts = mutableListOf<Food>()

    private val foodList = mutableListOf<Section>()

    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            getFood(bundle.getString("restaurant")!!)
        }
        return binding.root
    }

    private fun getFood(restaurant: String) {
        val foodQuery = database.child("Food").child(restaurant)
        foodQuery.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "An unexpected error occured.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val food = it.getValue(Food::class.java)
                    when (food!!.type) {
                        "predjelo" -> starters.add(food)
                        "glavno" -> mains.add(food)
                        "desert" -> deserts.add(food)
                    }
                }
                initRecyclerView()
            }

        })
    }

    private fun initRecyclerView() {
        adapter = FoodAdapter(requireContext(), foodList)
        val decorator = StickyHeaderItemDecorator(adapter)
        decorator.attachToRecyclerView(binding.recyclerView)
        adapter.onItemClick = {
            //todo open next step
        }
        adapter.addToCartClick = {
            //todo handle add to cart
        }
        val dividerItemDecoration =
            DividerItemDecoration(binding.recyclerView.context, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.adapter = adapter
        if (starters.isNotEmpty()) {
            val starterHeaderModel = HeaderModel(0)
            starterHeaderModel.header = "Predjela"
            foodList.add(starterHeaderModel)
            starters.forEach { starter ->
                val foodItem = ItemModel(0)
                foodItem.food = starter
                foodList.add(foodItem)
            }
        }
        if (mains.isNotEmpty()) {
            val mainHeaderModel = HeaderModel(starters.size)
            mainHeaderModel.header = "Glavna jela"
            foodList.add(mainHeaderModel)
            mains.forEach { main ->
                val foodItem = ItemModel(starters.size)
                foodItem.food = main
                foodList.add(foodItem)
            }
        }
        if (deserts.isNotEmpty()) {
            val desertHeaderModel = HeaderModel(starters.size + mains.size)
            desertHeaderModel.header = "Deserti"
            foodList.add(desertHeaderModel)
            deserts.forEach { desert ->
                val foodItem = ItemModel(starters.size + mains.size)
                foodItem.food = desert
                foodList.add(foodItem)
            }
        }
        adapter.notifyDataSetChanged()
    }

}