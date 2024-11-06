package com.knyazev.recipesapp.ui.resipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knyazev.recipesapp.Constants.PREFS_KEY_FAVORITES_CATEGORY
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.model.Recipe

data class RecipeState(
    val isFavorite: Boolean = false,
    val countPortions: Int = 1,
    val recipe: Recipe? = null,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val _recipeStateLD = MutableLiveData<RecipeState>()
    val recipeStateLD: LiveData<RecipeState> get() = _recipeStateLD

    init {
        Log.i("!!!", "init in RecipeViewModel")
        _recipeStateLD.value?.copy(isFavorite = true)
    }

    fun loadRecipe(recipeId: Int) {
        //TODO(): load from network
        RecipeState(recipe = STUB.getRecipeById(recipeId))
    }

    private fun getFavorites(): MutableSet<String> {
        val favorites =
            .getStringSet(PREFS_KEY_FAVORITES_CATEGORY, mutableSetOf()) ?: mutableSetOf()
        return HashSet(favorites)
    }
}