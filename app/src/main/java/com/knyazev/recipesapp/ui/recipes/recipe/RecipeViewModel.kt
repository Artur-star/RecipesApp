package com.knyazev.recipesapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.Constants.PREFS_KEY_FAVORITES_CATEGORY
import com.knyazev.recipesapp.Constants.PREFS_NAME
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

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _recipeStateLD = MutableLiveData(RecipeState())
    val recipeStateLD: LiveData<RecipeState> get() = _recipeStateLD
    private val recipesRepository: RecipesRepository by lazy { RecipesRepository(context = application.applicationContext) }

    init {
        Log.i("!!!", "init in RecipeViewModel")
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {

            val resultFromCache: Recipe? =
                recipesRepository.getRecipeByIdFromCache(recipeId)

            if (resultFromCache != null) {
                val isFavorite = getFavorites().contains(recipeId.toString())

                val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${resultFromCache.imageUrl}"

                _recipeStateLD.postValue(
                    _recipeStateLD.value?.copy(
                        isFavorite = isFavorite,
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
                    val isFavorite = getFavorites().contains(recipeId.toString())

                    val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${resultFromApi.imageUrl}"

                    _recipeStateLD.postValue(
                        _recipeStateLD.value?.copy(
                            isFavorite = isFavorite,
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

    private fun getFavorites(): MutableSet<String> {
        val favorites: MutableSet<String> =
            getApplication<Application>().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getStringSet(
                    PREFS_KEY_FAVORITES_CATEGORY, mutableSetOf()
                ) ?: mutableSetOf()
        return HashSet(favorites)
    }

    fun onFavoritesClicked(recipe: Recipe?) {
        viewModelScope.launch {
            recipesRepository.updateRecipeFromCache(recipe = recipe!!)
        }
        val copy = _recipeStateLD.value?.copy(isFavorite = recipe!!.isFavorite)
            ?: RecipeState(isFavorite = recipe!!.isFavorite)
        _recipeStateLD.value = copy
    }
}