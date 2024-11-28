package com.knyazev.recipesapp.ui.categories.categoryList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.databinding.FragmentListCategoriesBinding
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
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = try {
            STUB.getCategories()[categoryId]
        } catch (e: IllegalArgumentException) {
            throw e
        }

        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment2(
                category
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}