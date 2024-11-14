package com.knyazev.recipesapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_ID
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_IMAGE_URL
import com.knyazev.recipesapp.Constants.ARG_CATEGORY_NAME
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.model.Recipe

data class RecipeListState(
    val recipeList: List<Recipe> = emptyList(),
    val recipeListImage: Drawable? = null,
    val categoryName: String? = null,
)

class RecipeListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) :
    AndroidViewModel(application) {
    private val _recipesListStateLD = MutableLiveData<RecipeListState>()
    val recipesListStateLD get() = _recipesListStateLD

    fun loadRecipesList() {
        //TODO(): load from network
        val categoryImageUrl = savedStateHandle.get<String>(ARG_CATEGORY_IMAGE_URL)
        val categoryId = savedStateHandle.get<Int>(ARG_CATEGORY_ID)
        val categoryName = savedStateHandle.get<String>(ARG_CATEGORY_NAME)

        val recipeList: List<Recipe> = STUB.getRecipesByCategoryId(categoryId)
        val recipeListImage = try {
            Drawable.createFromStream(
                getApplication<Application>()
                    .applicationContext.assets.open(categoryImageUrl!!), null
            )
        } catch (e: NullPointerException) {
            Log.d("logTag", "Image not found $categoryImageUrl")
            null
        }
        val recipeListState =
            recipesListStateLD.value?.copy(recipeList, recipeListImage, categoryName)
                ?: RecipeListState(
                    recipeList,
                    recipeListImage,
                    categoryName
                )
        recipesListStateLD.value = recipeListState
    }
}