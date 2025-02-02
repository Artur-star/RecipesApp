package com.knyazev.recipesapp.ui.categories.categoryList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category
import kotlinx.coroutines.launch

data class CategoriesListState(
    val categoriesList: List<Category> = emptyList(),
    val error: Boolean = false,
)

class CategoriesListViewModel : ViewModel() {
    private val _categoriesListStateLD =
        MutableLiveData(CategoriesListState())
    val categoriesListStateLD: LiveData<CategoriesListState> get() = _categoriesListStateLD

    fun loadCategoryList() {
        viewModelScope.launch {
            val result: List<Category> = RecipesRepository().getCategories() ?: emptyList()

            _categoriesListStateLD.postValue(
                _categoriesListStateLD.value?.copy(
                    categoriesList = result,
                    error = false
                ) ?: CategoriesListState(categoriesList = result, error = true)
            )
        }
    }
}