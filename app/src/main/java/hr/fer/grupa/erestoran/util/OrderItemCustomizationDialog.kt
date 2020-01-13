package hr.fer.grupa.erestoran.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import hr.fer.grupa.erestoran.R
import hr.fer.grupa.erestoran.databinding.OrderItemCustomizationDialogBinding
import hr.fer.grupa.erestoran.models.Drink
import hr.fer.grupa.erestoran.models.Food

class OrderItemCustomizationDialog : BottomSheetDialogFragment() {

    private lateinit var binding: OrderItemCustomizationDialogBinding
    private lateinit var food: Food
    private lateinit var drink: Drink

    lateinit var listener: ItemSaveListener
    private var itemType = ""
    private var position = 0

    interface ItemSaveListener {
        fun saveFood(food: Food, position: Int)
        fun saveDrink(drink: Drink, position: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.order_item_customization_dialog,
            container,
            false
        )
        binding.fragment = this
        this.isCancelable = false
        position = arguments!!.getInt("position")
        if (arguments!!.getSerializable("food") != null) {
            food = arguments!!.getSerializable("food") as Food
            itemType = "food"
            setupFood(food)
        } else {
            drink = arguments!!.getSerializable("drink") as Drink
            itemType = "drink"
            setupDrink(drink)
        }
        return binding.root
    }

    private fun setupFood(food: Food) {
        binding.drinkSize.visibility = View.GONE
        if (!food.title.toLowerCase().contains("odrezak")) {
            binding.steakRareness.visibility = View.GONE
        } else {
            when (food.rareness) {
                0 -> binding.rarenessLevel.text = "Rare"
                1 -> binding.rarenessLevel.text = "Medium rare"
                2 -> binding.rarenessLevel.text = "Medium"
                3 -> binding.rarenessLevel.text = "Medium done"
                4 -> binding.rarenessLevel.text = "Well done"
            }
            binding.seekBar.progress = food.rareness
        }
        if (food.extras.isNotEmpty()) setupRecycler(food)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, isUser: Boolean) {
                when (progress) {
                    0 -> binding.rarenessLevel.text = "Rare"
                    1 -> binding.rarenessLevel.text = "Medium rare"
                    2 -> binding.rarenessLevel.text = "Medium"
                    3 -> binding.rarenessLevel.text = "Medium done"
                    4 -> binding.rarenessLevel.text = "Well done"
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun setupDrink(drink: Drink) {
        binding.steakRareness.visibility = View.GONE
        if (drink.extras.isNotEmpty()) setupDrinkRecycler(drink)
        else binding.foodExtras.visibility = View.GONE
        if (drink.type == "bezalkoholno") {
            when (drink.drinkSize) {
                "Small 0.3L" -> binding.small.isChecked = true
                "Medium 0.5L" -> binding.medium.isChecked = true
                "Large 0.7L" -> binding.large.isChecked = true
            }
        } else {
            binding.drinkSize.visibility = View.GONE
        }
    }

    private fun setupRecycler(food: Food) {
        val adapter = FoodExtrasAdapter(food.extras)
        adapter.onItemClick = { selectedFood, checked ->
            selectedFood.selected = checked
        }
        binding.foodExtras.adapter = adapter
    }

    private fun setupDrinkRecycler(drink: Drink) {
        val adapter = FoodExtrasAdapter(drink.extras)
        adapter.onItemClick = { selectedFood, checked ->
            selectedFood.selected = checked
        }
        binding.foodExtras.adapter = adapter
    }

    fun saveItem() {
        if (itemType == "food") {
            if (binding.steakRareness.visibility == View.VISIBLE) {
                food.rareness = binding.seekBar.progress
            }
            listener.saveFood(food, position)
        } else {
            when (binding.drinkSizeGroup.checkedRadioButtonId) {
                R.id.small -> drink.drinkSize = "Small 0.3L"
                R.id.medium -> drink.drinkSize = "Medium 0.5L"
                R.id.large -> drink.drinkSize = "Large 0.7L"
            }
            listener.saveDrink(drink, position)
        }
        this.dismiss()
    }

    fun cancelItem() {
        this.dismiss()
    }
}