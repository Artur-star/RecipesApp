package com.knyazev.recipesapp.ui.resipes.recipe

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.knyazev.recipesapp.data.entities.Ingredient

data class RecipeState(
    val ingredients: List<Ingredient> = emptyList(),
    val method: List<String> = emptyList(),
    val portions: Int = 1,
    val headerHeart: Boolean = false,
    val headerImage: Bitmap? = null,
    val headerText: String? = null,
)

class RecipeViewModel : ViewModel()