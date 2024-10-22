package com.knyazev.recipesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.knyazev.recipesapp.STUB
import com.knyazev.recipesapp.adapters.RecipesListAdapter
import com.knyazev.recipesapp.databinding.FragmentFavoritesListBinding
import com.knyazev.recipesapp.entities.Recipe

class FavoritesListFragment : Fragment() {

    private val recipeFragment = RecipeFragment()
    private val recipesListFragment = RecipesListFragment()
    private var _binding: FragmentFavoritesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for ActivityMainBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val setRecipes: List<Recipe> =
            STUB.getRecipesByIds(recipeFragment.getFavorites().map { it.toInt() }.toSet())
        val adapter = RecipesListAdapter(setRecipes)
        binding.rvFavorites.adapter = adapter
        if (setRecipes.isEmpty()) {
            binding.tvPlug.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvPlug.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
            adapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    recipesListFragment.openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}