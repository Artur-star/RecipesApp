package com.knyazev.recipesapp.ui.resipes.recipe

import androidx.lifecycle.ViewModel
import com.knyazev.recipesapp.model.Ingredient
import com.knyazev.recipesapp.model.Recipe

data class RecipeState(
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val isFavorite: Boolean = false,
    val recipe: Recipe? = null,
)

class RecipeViewModel : ViewModel()