package com.knyazev.recipesapp.ui.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.databinding.ItemMethodBinding

class MethodAdapter(var dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        val methodPoint = binding.methodPoint
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemMethodBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val methodPoint: String = dataSet[position]
        val numberPosition = position + 1

        holder.methodPoint.text = holder.methodPoint.context.resources.getString(
            R.string.position_in_method, numberPosition, methodPoint
        )
    }

    fun updateMethod(dataSet: List<String>) {
        this.dataSet = dataSet
    }
}