package com.knyazev.recipesapp.ui.categories.categoryList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category

data class CategoriesListState(
    val categoriesList: List<Category> = emptyList(),
)

class CategoriesListViewModel : ViewModel() {
    private val _categoriesListStateLD = MutableLiveData<CategoriesListState>()
    val categoriesListStateLD: LiveData<CategoriesListState> get() = _categoriesListStateLD

    fun loadCategoryList() {
        //TODO(): load from network
        try {
            val categoriesList = RecipesRepository().getCategories().get().toMutableList()
            val categoriesListState =
                _categoriesListStateLD.value?.copy(categoriesList = categoriesList)
                    ?: CategoriesListState(
                        categoriesList = categoriesList
                    )
            _categoriesListStateLD.value = categoriesListState
        } catch (e: Exception) {
            Log.e("!!!", "Exception network ${e.message}")
        } finally {
            RecipesRepository().shutdown()
        }

    }
}