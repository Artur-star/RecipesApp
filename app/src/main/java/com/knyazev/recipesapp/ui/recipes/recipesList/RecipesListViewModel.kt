package com.knyazev.recipesapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe

data class RecipeListState(
    val recipeList: List<Recipe> = emptyList(),
    val recipeListImage: Drawable? = null,
    val category: Category? = null,
)

class RecipeListViewModel(
    application: Application,
) :
    AndroidViewModel(application) {
    private val _recipesListStateLD = MutableLiveData<RecipeListState>()
    val recipesListStateLD get() = _recipesListStateLD

    fun loadRecipesList(category: Category) {
        RecipesRepository().getRecipesByCategoryId(category.id) { recipeList ->
            _recipesListStateLD.postValue(_recipesListStateLD.value?.copy(recipeList = recipeList!!))

            val recipeListImage = try {
                Drawable.createFromStream(
                    getApplication<Application>()
                        .applicationContext.assets.open(category.imageUrl), null
                )
            } catch (e: NullPointerException) {
                Log.d("logTag", "Image not found $category")
                null
            }
            val recipeListState =
                recipesListStateLD.value?.copy(recipeList!!, recipeListImage, category)
                    ?: RecipeListState(
                        recipeList!!,
                        recipeListImage,
                        category
                    )
            recipesListStateLD.value = recipeListState
        }
    }
}