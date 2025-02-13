package com.knyazev.recipesapp.di

import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.ui.categories.categoryList.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel =
        CategoriesListViewModel(recipesRepository)

}