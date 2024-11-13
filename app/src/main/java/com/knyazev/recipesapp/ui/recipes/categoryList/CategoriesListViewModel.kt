package com.knyazev.recipesapp.ui.recipes.categoryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.knyazev.recipesapp.data.STUB
import com.knyazev.recipesapp.model.Category

data class CategoriesListState(
    val categoriesList: List<Category> = emptyList(),
)

class CategoriesListViewModel : ViewModel() {
    private val _categoriesListStateLD = MutableLiveData<CategoriesListState>()
    val categoriesListStateLD: LiveData<CategoriesListState> get() = _categoriesListStateLD

    fun loadCategoryList() {
        //TODO(): load from network
        val categoriesList = STUB.getCategories().toMutableList()
        val categoriesListState =
            _categoriesListStateLD.value?.copy(categoriesList = categoriesList) ?: CategoriesListState(
                categoriesList = categoriesList
            )
        _categoriesListStateLD.value = categoriesListState
    }
}