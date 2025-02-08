package com.knyazev.recipesapp.ui.categories.categoryList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.model.Category
import kotlinx.coroutines.launch

data class CategoriesListState(
    val categoriesList: List<Category> = emptyList(),
    val error: Boolean = false,
)

class CategoriesListViewModel(private val application: Application) :
    AndroidViewModel(application) {
    private val _categoriesListStateLD =
        MutableLiveData(CategoriesListState())
    val categoriesListStateLD: LiveData<CategoriesListState> get() = _categoriesListStateLD

    fun loadCategoryList() {
        viewModelScope.launch {
            val resultFromCache: List<Category> =
                RecipesRepository(context = application.applicationContext).getCategoryFromCache()
            if (resultFromCache.isNotEmpty()) {
                _categoriesListStateLD.postValue(
                    _categoriesListStateLD.value?.copy(
                        categoriesList = resultFromCache,
                        error = false
                    ) ?: CategoriesListState(categoriesList = resultFromCache, error = true)
                )
            } else {
                val resultFromAPI: List<Category> =
                    RecipesRepository(context = application.applicationContext).getCategoryFromApi()
                        ?: emptyList()
                if (resultFromAPI.isNotEmpty()) {
                    _categoriesListStateLD.postValue(
                        _categoriesListStateLD.value?.copy(
                            categoriesList = resultFromAPI
                        )
                    )
                    RecipesRepository(context = application.applicationContext).addCategoriesToCache(
                        resultFromAPI
                    )
                } else {
                    _categoriesListStateLD.postValue(
                        _categoriesListStateLD.value?.copy(
                            error = true
                        )
                    )
                }
            }
        }
    }
}