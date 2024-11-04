package com.knyazev.recipesapp.ui.resipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knyazev.recipesapp.model.Recipe

data class RecipeState(
    val isFavorite: Boolean = false,
    val recipe: Recipe? = null,
)

class RecipeViewModel : ViewModel() {
    private var mutableRecipeStateLD = MutableLiveData<RecipeState>()
    val recipeStateLD: LiveData<RecipeState> get() = mutableRecipeStateLD

    init {
        Log.i("!!!", "init in RecipeViewModel")
        mutableRecipeStateLD.value = RecipeState(true)
    }
}