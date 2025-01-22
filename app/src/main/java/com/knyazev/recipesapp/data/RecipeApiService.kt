package com.knyazev.recipesapp.data

import com.knyazev.recipesapp.model.Category
import retrofit2.Call
import retrofit2.http.GET

public interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>
}