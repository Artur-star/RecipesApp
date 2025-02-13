package com.knyazev.recipesapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.RecipeApplication
import com.knyazev.recipesapp.databinding.FragmentRecipesListBinding
import com.knyazev.recipesapp.ui.recipes.adaptersRecipes.RecipesListAdapter

class RecipesListFragment : Fragment() {
    private val recipeListFragmentArgs: RecipesListFragmentArgs by navArgs()
    private var recipesListAdapter = RecipesListAdapter(emptyList())
    private lateinit var viewModel: RecipesListViewModel

    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for FragmentRecipesListBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        viewModel = appContainer.recipesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = recipeListFragmentArgs.categoryId
        viewModel.loadRecipesList(category)

        viewModel.recipesListStateLD.observe(viewLifecycleOwner) { (recipeList, recipeListImage, category, error) ->
            if (error) {
                Toast.makeText(
                    context,
                    R.string.error_receiving_data,
                    Toast.LENGTH_LONG
                ).show()
            }
            Glide.with(this)
                .load(recipeListImage)
                .error(R.drawable.img_error)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.ivHeaderRecipesList)
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
        findNavController().navigate(
            directions = RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            ),
        )
    }
}