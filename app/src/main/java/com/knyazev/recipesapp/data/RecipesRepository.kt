package com.knyazev.recipesapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.knyazev.recipesapp.Constants.REQUEST_URL
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit

class RecipesRepository {

    private val service: RecipeApiService by lazy { createService() }

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

    suspend fun getCategories(callback: (List<Category>?) -> Unit) {
        try {
            val categoryCall: Call<List<Category>> = service.getCategories()
            val categoryResponse = withContext(Dispatchers.IO) {
                categoryCall.execute()
            }
            callback(categoryResponse.body())


        } catch (e: Exception) {
            Log.i("network, getCategories()", "${e.printStackTrace()}")
            callback(null)
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        try {
            val recipeByCategoryIdCall = service.getRecipesByCategoryId(categoryId)
            val recipeByCategoryIdResponse =
                withContext(Dispatchers.IO) { recipeByCategoryIdCall.execute() }
            callback(recipeByCategoryIdResponse.body())
        } catch (e: Exception) {
            Log.i("network, getRecipesByCategoryId()", "${e.printStackTrace()}")
            callback(null)

        }
    }

    suspend fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        try {
            val recipeIdCall: Call<Recipe> = service.getRecipeId(recipeId)
            val recipeResponse = withContext(Dispatchers.IO) { recipeIdCall.execute() }
            callback(recipeResponse.body())
        } catch (e: Exception) {
            Log.i("network, getRecipeById()", "${e.printStackTrace()}")
            callback(null)
        }
    }

    suspend fun getRecipesByIds(setRecipesId: Set<Int>, callback: (List<Recipe>?) -> Unit) {
        try {
            val recipesByIdsCall: Call<List<Recipe>> =
                service.getRecipes(setRecipesId.joinToString(separator = ","))
            val recipeResponse = withContext(Dispatchers.IO) { recipesByIdsCall.execute() }
            callback(recipeResponse.body())
        } catch (e: Exception) {
            Log.i("network, getRecipesByIds()", "${e.printStackTrace()}")
            callback(null)
        }
    }
}
