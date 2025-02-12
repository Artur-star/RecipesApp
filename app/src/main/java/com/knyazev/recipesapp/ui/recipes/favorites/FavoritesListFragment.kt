package com.knyazev.recipesapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.knyazev.recipesapp.RecipeApplication
import com.knyazev.recipesapp.databinding.FragmentFavoritesListBinding
import com.knyazev.recipesapp.ui.recipes.adaptersRecipes.RecipesListAdapter

class FavoritesListFragment : Fragment() {
    private lateinit var viewModel: FavoritesListViewModel
    private val recipesListAdapter = RecipesListAdapter(emptyList())

    private var _binding: FragmentFavoritesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        viewModel = appContainer.favoritesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadFavoritesList()
        initRecycler()
    }

    private fun initRecycler() {
        viewModel.favoritesListStateLD.observe(viewLifecycleOwner) { (favorites) ->
            recipesListAdapter.updateRecipeList(favorites)
            if (favorites.isEmpty()) {
                binding.tvPlug.visibility = View.VISIBLE
                binding.rvFavorites.visibility = View.GONE
            } else {
                binding.tvPlug.visibility = View.GONE
                binding.rvFavorites.visibility = View.VISIBLE
            }
        }
        binding.rvFavorites.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            FavoritesListFragmentDirections.actionFavoritesListFragmentToRecipeFragment(recipeId),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}