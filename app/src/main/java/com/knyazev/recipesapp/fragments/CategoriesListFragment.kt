package com.knyazev.recipesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_ID
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_IMAGE_URL
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_NAME
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.STUB
import com.knyazev.recipesapp.adapters.CategoriesListAdapter
import com.knyazev.recipesapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val listAdapter = CategoriesListAdapter(STUB.getCategories())
        binding.rvCategories.adapter = listAdapter
        listAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories()[categoryId]
        val categoryName = category.title
        val categoryImageUrl = category.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
        }
    }
}