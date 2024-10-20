package com.knyazev.recipesapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knyazev.recipesapp.databinding.ItemIngredientsBinding
import com.knyazev.recipesapp.entities.Ingredient
import java.math.BigDecimal

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

    class ViewHolder(binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameIngredient = binding.nameIngredient
        val countIngredient = binding.countIngredient
        val unitOfMeasure = binding.unitOfMeasure
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemIngredientsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("!!!", " IngredientsAdapter - getItemCount ${dataSet.size}")
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        Log.d("!!!", " IngredientsAdapter - onBindViewHolder ${ingredient.description}")
        holder.nameIngredient.text = ingredient.description

        val totalQuantity = BigDecimal(ingredient.quantity) * BigDecimal(quantity)
        val formatCountIngr = totalQuantity.setScale(3).stripTrailingZeros().toPlainString()

        holder.countIngredient.text = formatCountIngr
        holder.unitOfMeasure.text = ingredient.unitOfMeasure
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}