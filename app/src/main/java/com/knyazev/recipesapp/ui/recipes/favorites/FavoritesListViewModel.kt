package com.knyazev.recipesapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.Constants.PREFS_KEY_FAVORITES_CATEGORY
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.launch

data class FavoritesListState(
    val favoritesList: List<Recipe> = emptyList(),
)

class FavoritesListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _favoritesListStateLD =
        MutableLiveData(FavoritesListState())
    val favoritesListStateLD get() = _favoritesListStateLD
    private val sharedPreferences = getApplication<Application>()
        .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)

    fun loadFavoritesList() {
        viewModelScope.launch {
            val favorites: List<Recipe> =
                RecipesRepository(context = application.applicationContext).getRecipesByIdsFromApi(
                    getFavorites().map { it.toInt() }.toSet()
                )
                    ?: emptyList()

            _favoritesListStateLD.postValue(
                _favoritesListStateLD.value?.copy(favoritesList = favorites) ?: FavoritesListState()
            )
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val favorites =
            sharedPreferences
                .getStringSet(PREFS_KEY_FAVORITES_CATEGORY, mutableSetOf()) ?: mutableSetOf()
        return HashSet(favorites)
    }
}