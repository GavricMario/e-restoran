package hr.fer.grupa.erestoran.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shuhart.stickyheader.StickyHeaderItemDecorator
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.databinding.FragmentFoodBinding
import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food
import hr.fer.grupa.erestoran.models.OrderFragmentEvent
import hr.fer.grupa.erestoran.util.OrderItemCustomizationDialog
import org.greenrobot.eventbus.EventBus

class FoodFragment : Fragment(), OrderItemCustomizationDialog.ItemSaveListener {

    private lateinit var binding: FragmentFoodBinding

    private val database = FirebaseDatabase.getInstance().reference

    private val starters = mutableListOf<Food>()
    private val mains = mutableListOf<Food>()
    private val deserts = mutableListOf<Food>()

    private val foodList = mutableListOf<Section>()

    private val selectedFood = mutableSetOf<Food>()

    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false)
        binding.fragment = this
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
        adapter.onItemClick = { section, position ->
            if (selectedFood.contains(section.getItem()) && (section.getItem().title.toLowerCase().contains(
                    "odrezak"
                ) || section.getItem().extras.isNotEmpty())
            ) {
                val foodOptionsDialog = OrderItemCustomizationDialog()
                foodOptionsDialog.listener = this
                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putSerializable("food", section.getItem())
                foodOptionsDialog.arguments = bundle
                foodOptionsDialog.show(requireActivity().supportFragmentManager, "ItemOptions")
            }
        }
        adapter.addToCartClick = { section, position ->
            if (!selectedFood.contains(section.getItem())) {
                if (section.getItem().title.toLowerCase().contains("odrezak") || section.getItem().extras.isNotEmpty()) {
                    val foodOptionsDialog = OrderItemCustomizationDialog()
                    foodOptionsDialog.listener = this
                    val bundle = Bundle()
                    bundle.putInt("position", position)
                    bundle.putSerializable("food", section.getItem())
                    foodOptionsDialog.arguments = bundle
                    foodOptionsDialog.show(requireActivity().supportFragmentManager, "ItemOptions")
                } else {
                    foodList[position].getItem().isInCart = true
                    selectedFood.add(section.getItem())
                    adapter.notifyItemChanged(position)
                }
            } else {
                selectedFood.remove(section.getItem())
                foodList[position].getItem().isInCart = false
                adapter.notifyItemChanged(position)
            }
        }
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

    fun finishFoodPicking() {
        EventBus.getDefault().post(OrderFragmentEvent(this, selectedFood))
    }

    override fun saveFood(food: Food, position: Int) {
        foodList[position].getItem().isInCart = true
        selectedFood.add(food)
        adapter.notifyItemChanged(position)
    }

    override fun saveDrink(drink: Drink, position: Int) {
    }

}