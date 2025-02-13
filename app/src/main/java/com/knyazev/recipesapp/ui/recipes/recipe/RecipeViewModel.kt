package com.knyazev.recipesapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.launch

data class RecipeState(
    val isFavorite: Boolean = false,
    val countPortions: Int = 1,
    val recipe: Recipe? = null,
    val recipeImageUrl: String? = null,
    val error: Boolean = false,
)

class RecipeViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _recipeStateLD = MutableLiveData(RecipeState())
    val recipeStateLD: LiveData<RecipeState> get() = _recipeStateLD

    init {
        Log.i("!!!", "init in RecipeViewModel")
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {

            val resultFromCache: Recipe? =
                recipesRepository.getRecipeByIdFromCache(recipeId)

            if (resultFromCache != null) {

                val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${resultFromCache.imageUrl}"

                _recipeStateLD.postValue(
                    _recipeStateLD.value?.copy(
                        isFavorite = resultFromCache.isFavorite,
                        recipe = resultFromCache,
                        recipeImageUrl = recipeImageUrl,
                        error = false
                    ) ?: RecipeState(
                        error = true
                    )
                )
            } else {
                val resultFromApi: Recipe? =
                    recipesRepository.getRecipeByIdFromApi(recipeId)

                if (resultFromApi != null) {

                    val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${resultFromApi.imageUrl}"

                    _recipeStateLD.postValue(
                        _recipeStateLD.value?.copy(
                            recipe = resultFromApi,
                            recipeImageUrl = recipeImageUrl,
                            error = false
                        )
                    )
                } else {
                    _recipeStateLD.postValue(
                        _recipeStateLD.value?.copy(
                            error = true
                        )
                    )
                }
            }
        }
    }

    fun setCountPortions(count: Int) {
        val recipeState =
            _recipeStateLD.value?.copy(countPortions = count) ?: RecipeState(countPortions = count)
        _recipeStateLD.value = recipeState
    }

    fun onFavoritesClicked(recipe: Recipe?) {
        viewModelScope.launch {
            if (recipe != null) {
                recipesRepository.updateRecipeFromCache(recipe = recipe)
                _recipeStateLD.postValue(
                    _recipeStateLD.value?.copy(isFavorite = recipe.isFavorite)
                        ?: RecipeState(isFavorite = recipe.isFavorite)
                )
            }
        }
    }
}