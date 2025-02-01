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
    application: Application,
) :
    AndroidViewModel(application) {
    private val _recipesListStateLD =
        MutableLiveData<RecipeListState>().apply { value = RecipeListState() }
    val recipesListStateLD get() = _recipesListStateLD

    fun loadRecipesList(category: Category) {
        viewModelScope.launch {
            val recipeList: List<Recipe> =
                RecipesRepository().getRecipesByCategoryId(category.id) ?: emptyList()

            val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${category.imageUrl}"

            recipesListStateLD.postValue(
                recipesListStateLD.value?.copy(
                    recipeList,
                    recipeImageUrl,
                    category,
                    error = false
                )
                    ?: RecipeListState(
                        recipeList,
                        recipeImageUrl,
                        category,
                        error = true
                    )
            )
        }
    }
}