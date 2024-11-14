package com.knyazev.recipesapp.ui.categories.categoryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.knyazev.recipesapp.Constants.ARG_CATEGORY
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.databinding.FragmentListCategoriesBinding
import com.knyazev.recipesapp.ui.categories.adaptersCategories.CategoriesListAdapter
import com.knyazev.recipesapp.ui.recipes.recipesList.RecipesListFragment

class CategoriesListFragment : Fragment() {
    private val categoriesListAdapter = CategoriesListAdapter(emptyList())
    private val viewModel: CategoriesListViewModel by viewModels()
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadCategoryList()
        initRecycler()
    }

    private fun initRecycler() {
        viewModel.categoriesListStateLD.observe(viewLifecycleOwner) { (categoriesList) ->
            categoriesListAdapter.updateCategoriesList(categoriesList)
        }
        binding.rvCategories.adapter = categoriesListAdapter
        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories()[categoryId]

        val bundle = bundleOf(
            ARG_CATEGORY to category
        )

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}