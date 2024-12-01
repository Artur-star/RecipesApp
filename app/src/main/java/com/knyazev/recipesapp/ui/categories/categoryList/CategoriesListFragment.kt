package com.knyazev.recipesapp.ui.categories.categoryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.R.anim
import com.knyazev.recipesapp.databinding.FragmentListCategoriesBinding
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.ui.categories.adaptersCategories.CategoriesListAdapter

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
            override fun onItemClick(category: Category) {
                openRecipesByCategoryId(category)
            }
        })
    }

    fun openRecipesByCategoryId(category: Category) {
        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            ),
            navOptions = NavOptions.Builder()
                .setEnterAnim(anim.nav_default_enter_anim)
                .setExitAnim(anim.nav_default_exit_anim)
                .setPopEnterAnim(anim.nav_default_pop_enter_anim)
                .setPopExitAnim(anim.nav_default_pop_exit_anim)
                .build()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}