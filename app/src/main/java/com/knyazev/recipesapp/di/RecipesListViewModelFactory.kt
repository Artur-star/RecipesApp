package com.knyazev.recipesapp.di

import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.ui.recipes.recipesList.RecipesListViewModel

class RecipesListViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipesListViewModel> {
    override fun create(): RecipesListViewModel = RecipesListViewModel(recipesRepository)
}