package com.knyazev.recipesapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.launch

data class RecipeListState(
    val recipeList: List<Recipe> = emptyList(),
    val recipeListImageUrl: String? = null,
    val category: Category? = null,
    val error: Boolean = false,
)

class RecipeListViewModel(
    private val application: Application,
) :
    AndroidViewModel(application) {
    private val _recipesListStateLD =
        MutableLiveData(RecipeListState())
    val recipesListStateLD get() = _recipesListStateLD

    fun loadRecipesList(category: Category) {
        viewModelScope.launch {
            val resultFromCache: List<Recipe> =
                RecipesRepository(context = application.applicationContext).getRecipesByCategoryIdFromCache(category.id)
            if (resultFromCache.isNotEmpty()) {
                val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${category.imageUrl}"

                recipesListStateLD.postValue(
                    recipesListStateLD.value?.copy(
                        resultFromCache,
                        recipeImageUrl,
                        category,
                        error = false
                    )
                        ?: RecipeListState(
                            resultFromCache,
                            recipeImageUrl,
                            category,
                            error = true
                        )
                )
            }

            val resultFromApi: List<Recipe> =
                RecipesRepository(context = application.applicationContext).getRecipesByCategoryIdFromAPI(
                    category.id
                ) ?: emptyList()

            if (resultFromApi.isNotEmpty()) {
                val result: List<Recipe> = resultFromApi.map { recipe: Recipe -> recipe.copy(categoryId = category.id) }

                val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${category.imageUrl}"

                recipesListStateLD.postValue(
                    recipesListStateLD.value?.copy(
                        result,
                        recipeImageUrl,
                        category,
                        error = false
                    )
                        ?: RecipeListState(
                            result,
                            recipeImageUrl,
                            category,
                            error = true
                        )
                )

                RecipesRepository(context = application.applicationContext).addRecipesToCache(
                    result
                )
            }
        }
    }
}