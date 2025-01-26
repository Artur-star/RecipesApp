package com.knyazev.recipesapp.ui.categories.categoryList

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
        RecipesRepository().getCategories { resultCategory ->
            _categoriesListStateLD.postValue(
                _categoriesListStateLD.value?.copy(categoriesList = resultCategory!!)
                    ?: CategoriesListState(
                        categoriesList = resultCategory!!
                    )
            )
        }
    }
}