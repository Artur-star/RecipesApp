package com.knyazev.recipesapp.di

import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel = RecipeViewModel(recipesRepository)
}