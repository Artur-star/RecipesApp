package com.knyazev.recipesapp.ui.recipes.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.launch

data class FavoritesListState(
    val favoritesList: List<Recipe> = emptyList(),
)

class FavoritesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _favoritesListStateLD =
        MutableLiveData(FavoritesListState())
    val favoritesListStateLD get() = _favoritesListStateLD

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