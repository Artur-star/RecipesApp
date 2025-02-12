package com.knyazev.recipesapp.data

import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {

    @GET("recipe/{id}")
    fun getRecipeId(@Path("id") id: Int): Call<Recipe>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}