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

class CategoriesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {
    private val _categoriesListStateLD =
        MutableLiveData(CategoriesListState())
    val categoriesListStateLD: LiveData<CategoriesListState> get() = _categoriesListStateLD

    fun loadCategoryList() {
        viewModelScope.launch {
            val resultFromCache: List<Category> =
                recipesRepository.getCategoryFromCache()
            if (resultFromCache.isNotEmpty()) {
                _categoriesListStateLD.postValue(
                    _categoriesListStateLD.value?.copy(
                        categoriesList = resultFromCache,
                        error = false
                    ) ?: CategoriesListState(categoriesList = resultFromCache, error = true)
                )
            } else {
                val resultFromAPI: List<Category> =
                    recipesRepository.getCategoryFromApi()
                        ?: emptyList()
                if (resultFromAPI.isNotEmpty()) {
                    _categoriesListStateLD.postValue(
                        _categoriesListStateLD.value?.copy(
                            categoriesList = resultFromAPI
                        )
                    )
                    recipesRepository.addCategoriesToCache(
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