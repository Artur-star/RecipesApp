package com.knyazev.recipesapp.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.knyazev.recipesapp.Constants.REQUEST_URL
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RecipesRepository {

    private val threadPool = Executors.newFixedThreadPool(10)

    private fun createService(): RecipeApiService {

        val contentType: MediaType = "application/json".toMediaType()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(REQUEST_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        return retrofit.create(RecipeApiService::class.java)
    }

    fun getCategories(): Future<List<Category>> {
        val categoriesCall: Call<List<Category>> = createService().getCategories()
        return threadPool.submit<List<Category>> {
            categoriesCall.execute().body() ?: emptyList()
        }
    }

    fun getRecipesByCategoryId(categoryId: Int): Future<List<Recipe>> {
        val recipesByCategoryIdCall: Call<List<Recipe>> =
            createService().getRecipesByCategoryId(categoryId)

        return threadPool.submit<List<Recipe>> {
            recipesByCategoryIdCall.execute().body() ?: emptyList()
        }
    }

    fun getRecipeById(recipeId: Int): Future<Recipe> {
        val recipeIdCall: Call<Recipe> = createService().getRecipeId(recipeId)
        return threadPool.submit<Recipe> {
            recipeIdCall.execute().body() ?: throw NullPointerException("RecipeId is not found")
        }
    }

    fun getRecipesByIds(setRecipesId: Set<Int>): Future<List<Recipe>> {
        val recipesCall: Call<List<Recipe>> =
            createService().getRecipes(setRecipesId.joinToString(separator = ","))
        return threadPool.submit<List<Recipe>> {
            recipesCall.execute().body() ?: emptyList()
        }
    }

    fun shutdown() {
        threadPool.shutdown()
    }
}
