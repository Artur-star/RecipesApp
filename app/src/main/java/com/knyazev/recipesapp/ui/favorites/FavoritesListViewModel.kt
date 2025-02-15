package com.knyazev.recipesapp.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesListState(
    val favoritesList: List<Recipe> = emptyList(),
)

@HiltViewModel
class FavoritesListViewModel @Inject constructor(
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