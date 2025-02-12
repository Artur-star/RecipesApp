package com.knyazev.recipesapp.data

import android.util.Log
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call

class RecipesRepository(
    private val recipeDao: RecipeDao,
    private val categoryDao: CategoryDao,
    private val service: RecipeApiService,
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend fun addRecipesToCache(recipes: List<Recipe>) {
        recipeDao.addRecipes(recipes)
    }

    suspend fun addCategoriesToCache(categories: List<Category>) {
        categoryDao.addCategories(categories)
    }

    suspend fun getCategoryFromCache(): List<Category> {
        return categoryDao.getAllCategories()
    }

    suspend fun getRecipeFromCache(): List<Recipe> {
        return recipeDao.getAllRecipes()
    }

    suspend fun getRecipeByIdFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)
    }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int): List<Recipe> {
        return recipeDao.getRecipesByCategoryId(categoryId)
    }

    suspend fun updateRecipeFromCache(recipe: Recipe) {
        recipe.isFavorite = !recipe.isFavorite
        recipeDao.updateRecipe(recipe)
    }

    suspend fun getCategoryFromApi(): List<Category>? {
        try {
            val categoryCall: Call<List<Category>> = service.getCategories()
            val categoryResponse = withContext(dispatcherIO) {
                categoryCall.execute()
            }
            return categoryResponse.body()
        } catch (e: Exception) {
            Log.i("network, getCategories()", "${e.printStackTrace()}")
            return null
        }
    }

    suspend fun getRecipesByCategoryIdFromApi(categoryId: Int): List<Recipe>? {
        try {
            val recipeByCategoryIdCall = service.getRecipesByCategoryId(categoryId)
            val recipeByCategoryIdResponse =
                withContext(dispatcherIO) { recipeByCategoryIdCall.execute() }
            return recipeByCategoryIdResponse.body()
        } catch (e: Exception) {
            Log.i("network, getRecipesByCategoryId()", "${e.printStackTrace()}")
            return null
        }
    }

    suspend fun getRecipeByIdFromApi(recipeId: Int): Recipe? {
        try {
            val recipeIdCall: Call<Recipe> = service.getRecipeId(recipeId)
            val recipeResponse = withContext(dispatcherIO) { recipeIdCall.execute() }
            return recipeResponse.body()
        } catch (e: Exception) {
            Log.i("network, getRecipeById()", "${e.printStackTrace()}")
            return null
        }
    }
}
