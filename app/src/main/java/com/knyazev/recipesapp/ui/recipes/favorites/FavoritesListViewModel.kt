package com.knyazev.recipesapp.ui.recipes.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
    private val recipesRepository: RecipesRepository by lazy { RecipesRepository(context = application.applicationContext) }

    fun loadFavoritesList() {
        viewModelScope.launch {

            val favoritesFromCache: List<Recipe> = recipesRepository.getRecipeFromCache()
                .filter { recipe: Recipe -> recipe.isFavorite }

            _favoritesListStateLD.postValue(
                _favoritesListStateLD.value?.copy(favoritesList = favoritesFromCache)
                    ?: FavoritesListState()
            )
        }
    }
}