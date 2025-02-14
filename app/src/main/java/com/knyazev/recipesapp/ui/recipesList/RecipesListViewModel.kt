package com.knyazev.recipesapp.ui.recipesList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeListState(
    val recipeList: List<Recipe> = emptyList(),
    val recipeListImageUrl: String? = null,
    val category: Category? = null,
    val error: Boolean = false,
)

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _recipesListStateLD =
        MutableLiveData(RecipeListState())
    val recipesListStateLD get() = _recipesListStateLD

    fun loadRecipesList(category: Category) {
        viewModelScope.launch {
            val resultFromCache: List<Recipe> =
                recipesRepository.getRecipesByCategoryIdFromCache(category.id)
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
            } else {
                val resultFromApi: List<Recipe> =
                    recipesRepository.getRecipesByCategoryIdFromApi(
                        category.id
                    )?.map { recipe: Recipe -> recipe.copy(categoryId = category.id) }
                        ?: emptyList()

                if (resultFromApi.isNotEmpty()) {
                    val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${category.imageUrl}"

                    recipesListStateLD.postValue(
                        recipesListStateLD.value?.copy(
                            resultFromApi,
                            recipeImageUrl,
                            category,
                        )
                    )
                    recipesRepository.addRecipesToCache(resultFromApi)
                } else {
                    recipesListStateLD.postValue(
                        recipesListStateLD.value?.copy(
                            error = true
                        )
                    )
                }
            }
        }
    }
}