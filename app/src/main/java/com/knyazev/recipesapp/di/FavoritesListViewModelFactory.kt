package com.knyazev.recipesapp.di

import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.ui.recipes.favorites.FavoritesListViewModel

class FavoritesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoritesListViewModel> {
    override fun create(): FavoritesListViewModel = FavoritesListViewModel(recipesRepository)
}