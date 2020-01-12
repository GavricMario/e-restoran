package hr.fer.grupa.erestoran.drink

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
import hr.fer.grupa.erestoran.databinding.DrinkFragmentBinding
import hr.fer.grupa.erestoran.food.HeaderModel
import hr.fer.grupa.erestoran.food.ItemModel
import hr.fer.grupa.erestoran.food.Section
import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food
import hr.fer.grupa.erestoran.models.OrderFragmentEvent
import hr.fer.grupa.erestoran.util.OrderItemCustomizationDialog
import org.greenrobot.eventbus.EventBus

class DrinkFragment : Fragment(), OrderItemCustomizationDialog.ItemSaveListener {

    private lateinit var binding: DrinkFragmentBinding

    private val database = FirebaseDatabase.getInstance().reference

    private val nonAlcoholic = mutableListOf<Drink>()
    private val alcoholic = mutableListOf<Drink>()
    private val warmDrinks = mutableListOf<Drink>()

    private val drinkList = mutableListOf<Section>()

    private val selectedDrinks = mutableSetOf<Drink>()

    private lateinit var adapter: DrinkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.drink_fragment, container, false)
        binding.fragment = this
        val bundle = this.arguments
        if (bundle != null) {
            getDrinks(bundle.getString("restaurant")!!)
        }
        return binding.root
    }

    private fun getDrinks(restaurant: String) {
        val drinkQuery = database.child("Drink").child(restaurant)
        drinkQuery.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "An unexpected error occured.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val drink = it.getValue(Drink::class.java)
                    when (drink!!.type) {
                        "bezalkoholno" -> nonAlcoholic.add(drink)
                        "alkoholno" -> alcoholic.add(drink)
                        "toplo" -> warmDrinks.add(drink)
                    }
                }
                initRecyclerView()
            }

        })
    }

    private fun initRecyclerView() {
        adapter = DrinkAdapter(requireContext(), drinkList)
        val decorator = StickyHeaderItemDecorator(adapter)
        decorator.attachToRecyclerView(binding.recyclerView)
        adapter.onItemClick = { section, position ->
            if (selectedDrinks.contains(section.getDrinkItem()) && section.getDrinkItem().type != "toplo") {
                val drinkOptionsDialog = OrderItemCustomizationDialog()
                drinkOptionsDialog.listener = this
                val bundle = Bundle()
                bundle.putInt("position", position)
                bundle.putSerializable("drink", section.getDrinkItem())
                drinkOptionsDialog.arguments = bundle
                drinkOptionsDialog.show(requireActivity().supportFragmentManager, "ItemOptions")
            }
        }
        adapter.addToCartClick = { section, position ->
            if (!selectedDrinks.contains(section.getDrinkItem())) {
                if (section.getDrinkItem().type != "toplo") {
                    val drinkOptionsDialog = OrderItemCustomizationDialog()
                    drinkOptionsDialog.listener = this
                    val bundle = Bundle()
                    bundle.putInt("position", position)
                    bundle.putSerializable("drink", section.getDrinkItem())
                    drinkOptionsDialog.arguments = bundle
                    drinkOptionsDialog.show(requireActivity().supportFragmentManager, "ItemOptions")
                } else {
                    drinkList[position].getDrinkItem().isInCart = true
                    selectedDrinks.add(section.getDrinkItem())
                    adapter.notifyItemChanged(position)
                }
            } else {
                selectedDrinks.remove(section.getDrinkItem())
                drinkList[position].getDrinkItem().isInCart = false
                adapter.notifyItemChanged(position)
            }
        }
        binding.recyclerView.adapter = adapter
        if (nonAlcoholic.isNotEmpty()) {
            val starterHeaderModel = HeaderModel(0)
            starterHeaderModel.header = getString(R.string.bezalkoholna_pica)
            drinkList.add(starterHeaderModel)
            nonAlcoholic.forEach { nonAlcoholic ->
                val drinkItem = ItemModel(0)
                drinkItem.drink = nonAlcoholic
                drinkList.add(drinkItem)
            }
        }
        if (alcoholic.isNotEmpty()) {
            val mainHeaderModel = HeaderModel(nonAlcoholic.size)
            mainHeaderModel.header = getString(R.string.alkoholna_pica)
            drinkList.add(mainHeaderModel)
            alcoholic.forEach { alcoholic ->
                val drinkItem = ItemModel(nonAlcoholic.size)
                drinkItem.drink = alcoholic
                drinkList.add(drinkItem)
            }
        }
        if (warmDrinks.isNotEmpty()) {
            val desertHeaderModel = HeaderModel(nonAlcoholic.size + alcoholic.size)
            desertHeaderModel.header = getString(R.string.topli_napitci)
            drinkList.add(desertHeaderModel)
            warmDrinks.forEach { warm ->
                val drinkItem = ItemModel(nonAlcoholic.size + alcoholic.size)
                drinkItem.drink = warm
                drinkList.add(drinkItem)
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun finishDrinkPicking() {
        EventBus.getDefault().post(OrderFragmentEvent(this, selectedDrinks))
    }

    override fun saveFood(food: Food, position: Int) {
    }

    override fun saveDrink(drink: Drink, position: Int) {
        drinkList[position].getDrinkItem().isInCart = true
        selectedDrinks.add(drink)
        adapter.notifyItemChanged(position)
    }
}