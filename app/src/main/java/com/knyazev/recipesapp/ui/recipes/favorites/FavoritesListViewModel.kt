package com.knyazev.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.Constants.PREFS_KEY_FAVORITES_CATEGORY
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Recipe

data class FavoritesListState(
    val favoritesList: List<Recipe> = emptyList(),
)

class FavoritesListViewModel(application: Application) : AndroidViewModel(application) {
    private val _favoritesListStateLD = MutableLiveData<FavoritesListState>()
    val favoritesListStateLD get() = _favoritesListStateLD
    private val sharedPreferences = getApplication<Application>()
        .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun loadFavoritesList() {
        val favorites = try {
            RecipesRepository().getRecipesByIds(getFavorites().map { it.toInt() }.toSet()).get()
        } catch (e: Exception) {
            Log.e("!!!", "Exception network ${e.message}")
            null
        } finally {
            RecipesRepository().shutdown()
        }

        if (favorites == null) {
            Toast.makeText(
                Application().applicationContext,
                "Ошибка получения данных",
                Toast.LENGTH_LONG
            ).show()
        }

        val favoritesListState =
            _favoritesListStateLD.value?.copy(favoritesList = favorites!!) ?: FavoritesListState(
                favoritesList = favorites!!
            )
        _favoritesListStateLD.value = favoritesListState
    }

    private fun getFavorites(): MutableSet<String> {
        val favorites =
            sharedPreferences
                .getStringSet(PREFS_KEY_FAVORITES_CATEGORY, mutableSetOf()) ?: mutableSetOf()
        return HashSet(favorites)
    }
}