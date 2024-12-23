package com.knyazev.recipesapp.ui.categories.adaptersCategories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knyazev.recipesapp.databinding.ItemCategoryBinding
import com.knyazev.recipesapp.model.Category

class CategoriesListAdapter(var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleTextView: TextView = binding.titleTextView
        val imageView: ImageView = binding.imageView
        val descriptionTextView: TextView = binding.descriptionTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: ItemCategoryBinding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
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

        viewHolder.itemView.setOnClickListener { itemClickListener?.onItemClick(category) }
    }

    fun updateCategoriesList(dataSet: List<Category>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}