package com.knyazev.recipesapp.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knyazev.recipesapp.databinding.ItemCategoryBinding
import com.knyazev.recipesapp.entities.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        Log.d("!!!", "setOnItemClickListener in CategoriesListAdapter")
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleTextView: TextView = binding.titleTextView
        val imageView: ImageView = binding.imageView
        val descriptionTextView: TextView = binding.descriptionTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("!!!", "onCreateViewHolder in CategoriesListAdapter")
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemCategoryBinding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("!!!", "getItemCount in CategoriesListAdapter")
        return dataSet.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d("!!!", "onBindViewHolder in CategoriesListAdapter")
        val category = dataSet[position]

        viewHolder.titleTextView.text = category.title
        viewHolder.descriptionTextView.text = category.description
        viewHolder.imageView.contentDescription = category.title

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.imageView.context.assets.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.d("logTag", "Image not found: ${category.imageUrl}")
            null
        }
        viewHolder.imageView.setImageDrawable(drawable)

        viewHolder.itemView.setOnClickListener { itemClickListener?.onItemClick(category.id) }

    }
}