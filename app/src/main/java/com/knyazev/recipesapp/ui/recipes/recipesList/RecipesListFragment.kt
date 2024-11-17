package com.knyazev.recipesapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.knyazev.recipesapp.Constants.ARG_RECIPE
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.databinding.FragmentRecipesListBinding
import com.knyazev.recipesapp.ui.recipes.adaptersRecipes.RecipesListAdapter

class RecipesListFragment : Fragment() {
    private var recipesListAdapter = RecipesListAdapter(emptyList())
    private val viewModel: RecipeListViewModel by viewModels()

    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipesListBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.loadRecipesList()
        viewModel.recipesListStateLD.observe(viewLifecycleOwner) { (recipeList, recipeListImage, category) ->
            binding.ivHeaderRecipesList.setImageDrawable(recipeListImage)
            binding.tvHeaderRecipes.text = category?.title
            recipesListAdapter.updateRecipeList(recipeList)
        }
        binding.rvRecipes.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(
            ARG_RECIPE to recipe
        )

        findNavController().navigate(R.id.action_recipesListFragment_to_recipeFragment, bundle)
    }
}