package com.knyazev.recipesapp.ui.recipes.recipesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.knyazev.recipesapp.Constants
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe

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
        RecipesRepository().getRecipesByCategoryId(category.id) { recipeListIsNull ->
            val recipeList = recipeListIsNull ?: emptyList()

            val recipeImageUrl = "${Constants.REQUEST_IMAGE_URL}${category.imageUrl}"

            recipesListStateLD.postValue(
                recipesListStateLD.value?.copy(recipeList, recipeImageUrl, category, error = false)
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