package com.knyazev.recipesapp.ui.recipes.adaptersRecipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knyazev.recipesapp.databinding.ItemRecipeBinding
import com.knyazev.recipesapp.model.Recipe

class RecipesListAdapter(var dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.imageView
        val textView: TextView = binding.titleTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe: Recipe = dataSet[position]

        holder.textView.text = recipe.title

        val drawable = try {
            Drawable.createFromStream(
                holder.imageView.context.assets.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.d("logTag", "Image not found: ${recipe.imageUrl}")
            null
        }
        holder.imageView.setImageDrawable(drawable)

        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
    }

    fun updateRecipeList(dataSet: List<Recipe>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}