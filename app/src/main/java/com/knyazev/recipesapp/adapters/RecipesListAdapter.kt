package com.knyazev.recipesapp.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knyazev.recipesapp.databinding.ItemRecipeBinding
import com.knyazev.recipesapp.entities.Recipe

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        Log.d("!!!", "setOnItemClickListener in RecipesListAdapter")
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.imageView
        val textView: TextView = binding.titleTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("!!!", "onCreateViewHolder in RecipesListAdapter")
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("!!!", "getItemCount in RecipesListAdapter")
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("!!!", "onBindViewHolder in RecipesListAdapter")
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
}