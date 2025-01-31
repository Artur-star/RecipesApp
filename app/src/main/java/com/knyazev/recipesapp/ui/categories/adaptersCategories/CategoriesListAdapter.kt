package com.knyazev.recipesapp.ui.categories.adaptersCategories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.R
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = dataSet[position]
        holder.titleTextView.text = category.title
        holder.descriptionTextView.text = category.description
        holder.imageView.contentDescription = category.title

        Glide.with(holder.itemView.context)
            .load("${Constants.REQUEST_IMAGE_URL}${category.imageUrl}")
            .error(R.drawable.img_error)
            .placeholder(R.drawable.img_placeholder)
            .into(holder.imageView)

        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(category) }
    }

    fun updateCategoriesList(dataSet: List<Category>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }
}