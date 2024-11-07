package com.knyazev.recipesapp.ui.resipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knyazev.recipesapp.Constants.PREFS_KEY_FAVORITES_CATEGORY
import com.knyazev.recipesapp.Constants.PREFS_NAME
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
    }

    fun loadRecipe(recipeId: Int) {
        //TODO(): load from network

        RecipeState(
            recipe = STUB.getRecipeById(recipeId),
            isFavorite = getFavorites().contains(recipeId.toString()),
            countPortions = 1
        )
    }

    private fun getFavorites(): MutableSet<String> {
        val favorites: MutableSet<String> =
            getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(
                    PREFS_KEY_FAVORITES_CATEGORY, mutableSetOf()
                ) ?: mutableSetOf()
        return HashSet(favorites)
    }

    fun onFavoritesClicked(recipeId: Int) {
        val favorite = getFavorites().contains(recipeId.toString())
        val favorites = getFavorites()

        if (favorite) favorites.remove(recipeId.toString())
        else favorites.add(recipeId.toString())

        saveFavorites(favorites)
        _recipeStateLD.value?.copy(isFavorite = favorites.contains(recipeId.toString()))
    }

    private fun saveFavorites(recipeId: Set<String>) {
        getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putStringSet(PREFS_KEY_FAVORITES_CATEGORY, recipeId)
            .apply()
    }
}