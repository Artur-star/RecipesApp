package com.knyazev.recipesapp.data

import retrofit2.Retrofit



class RecipesRepository {
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .build()

    var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
}