package com.knyazev.recipesapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_NAME
import com.knyazev.recipesapp.Constants.ARG_RECIPE
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.databinding.FragmentRecipesListBinding
import com.knyazev.recipesapp.ui.recipes.adapters.RecipesListAdapter
import com.knyazev.recipesapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()
    private var categoryName: String? = null

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

        categoryName = requireArguments().getString(ARG_CATEGORY_NAME)
        binding.tvHeaderRecipes.text = categoryName
        viewModel.loadRecipesList()
        viewModel.recipesListStateLD.observe(viewLifecycleOwner) { (recipeList, recipeListImage) ->
            binding.ivHeaderRecipesList.setImageDrawable(recipeListImage)
            val listAdapter = RecipesListAdapter(recipeList)
            binding.rvRecipes.adapter = listAdapter
            listAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(
            ARG_RECIPE to recipe
        )
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
        }
    }
}