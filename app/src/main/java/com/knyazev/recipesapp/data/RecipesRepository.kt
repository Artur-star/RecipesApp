package com.knyazev.recipesapp.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.knyazev.recipesapp.Constants.REQUEST_URL
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit

class RecipesRepository(
    private val dispatcherIO: CoroutineDispatcher = Dispatchers.IO,
    val context: Context,
) {

    private val service: RecipeApiService by lazy { createService() }
    private val db: RecipeDatabase by lazy {
        Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "database-users"
        ).fallbackToDestructiveMigration().build()
    }
    private val categoryDao: CategoryDao = db.categoryDao()
    private val recipeDao: RecipeDao = db.recipeDao()

    private fun createService(): RecipeApiService {

        val contentType: MediaType = "application/json".toMediaType()

        val logger: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(REQUEST_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(client)
            .build()
        return retrofit.create(RecipeApiService::class.java)
    }

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

    suspend fun getRecipesByIdsFromCache(ids: List<Int>): List<Recipe>? {
        return recipeDao.getRecipesByIds(ids)
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

    suspend fun getRecipesByIdsFromApi(setRecipesId: Set<Int>): List<Recipe>? {
        try {
            val recipesByIdsCall: Call<List<Recipe>> =
                service.getRecipes(setRecipesId.joinToString(separator = ","))
            val recipeResponse = withContext(dispatcherIO) { recipesByIdsCall.execute() }
            return recipeResponse.body()
        } catch (e: Exception) {
            Log.i("network, getRecipesByIds()", "${e.printStackTrace()}")
            return null
        }
    }
}
