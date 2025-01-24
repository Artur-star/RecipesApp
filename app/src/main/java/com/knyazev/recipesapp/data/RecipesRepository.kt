package com.knyazev.recipesapp.data

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.knyazev.recipesapp.Constants.REQUEST_URL
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
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
//        return try {
            return threadPool.submit<List<Category>> {
                categoriesCall.execute().body() ?: emptyList()

//                    Log.d("!!!", "getCategoriesIN ${categoryExecute?.body()}")
            }
//        } catch (e: Exception) {
//            Log.e("!!!", "Exception network ${e.message}")
//        } finally {
//            threadPool.shutdown()
//        }
        //Log.d("!!!", "getCategories ${categoryExecute?.body()}")
    }

    fun shutdown() {
        threadPool.shutdown()
    }


    fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        val recipesByCategoryIdCall: Call<List<Recipe>> = createService().getRecipesByCategoryId(categoryId)
        var recipesByCategoryIdResponse: Response<List<Recipe>>? = null
        try {
            threadPool.submit {
                recipesByCategoryIdResponse = recipesByCategoryIdCall.execute()
            }
        } catch (e: Exception) {
            Log.e("!!!", "Exception network ${e.message}")
        } finally {
            threadPool.shutdown()
        }

        Log.d("!!!", "getRecipesByCategoryId ${recipesByCategoryIdResponse?.body()}")

        return recipesByCategoryIdResponse?.body() ?: emptyList()
    }

    fun getRecipeById(recipeId: Int): Recipe {
        val recipeIdCall: Call<Recipe> = createService().getRecipeId(recipeId)
        var recipeIdResponse: Response<Recipe>? = null
        try {
            threadPool.submit {
                recipeIdResponse =
                    recipeIdCall.execute()
            }
        } catch (e: Exception) {
            Log.e("!!!", "Exception network ${e.message}")
        } finally {
            threadPool.shutdown()
        }

        Log.d("!!!", "getRecipeById ${recipeIdResponse?.body()}")
        return recipeIdResponse?.body() ?: throw NullPointerException("RecipeId is not found")
    }

    fun getRecipesByIds(setRecipesId: Set<Int>): List<Recipe> {
        val recipesCall: Call<List<Recipe>> = createService().getRecipes(setRecipesId.joinToString(separator = ","))
        var recipeResponse: Response<List<Recipe>>? = null
        try {
            threadPool.submit {
                recipeResponse = recipesCall.execute()
            }
        } catch (e: Exception) {
            Log.e("!!!", "Exception network ${e.message}")
        } finally {
            threadPool.shutdown()
        }
        Log.d("!!!", "getRecipesByIds ${recipeResponse?.body()}")
        return recipeResponse?.body() ?: emptyList()
    }
}

//    val categoryIdCall: Call<Category> = service.getCategoryId(1)
//    val categoryIdResponse = categoryIdCall.execute()
//    val categoryId = categoryIdResponse.body()
