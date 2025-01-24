package com.knyazev.recipesapp.ui.categories.categoryList

import android.app.Application
import android.util.Log
import android.widget.Toast
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
        val categoriesList: MutableList<Category>? = try {
            RecipesRepository().getCategories().get().toMutableList()
        } catch (e: Exception) {
            Log.e("!!!", "Exception network ${e.message}")
            null
        } finally {
            RecipesRepository().shutdown()
        }

        if (categoriesList == null) {
            Toast.makeText(
                Application().applicationContext,
                "Ошибка получения данных",
                Toast.LENGTH_LONG
            ).show()
        }

        val categoriesListState =
            _categoriesListStateLD.value?.copy(categoriesList = categoriesList!!)
                ?: CategoriesListState(
                    categoriesList = categoriesList!!
                )
        _categoriesListStateLD.value = categoriesListState
    }
}