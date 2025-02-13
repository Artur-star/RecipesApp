package com.knyazev.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.knyazev.recipesapp.Constants.REQUEST_URL
import com.knyazev.recipesapp.data.CategoryDao
import com.knyazev.recipesapp.data.RecipeApiService
import com.knyazev.recipesapp.data.RecipeDao
import com.knyazev.recipesapp.data.RecipeDatabase
import com.knyazev.recipesapp.data.RecipesRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {
    private val db: RecipeDatabase by lazy {
        Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "database-users"
        ).fallbackToDestructiveMigration().build()
    }
    private val categoryDao: CategoryDao = db.categoryDao()
    private val recipeDao: RecipeDao = db.recipeDao()
    private val contentType: MediaType = "application/json".toMediaType()
    private val logger: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logger).build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(REQUEST_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .client(client)
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val recipesRepository =
        RecipesRepository(recipeDao = recipeDao, categoryDao = categoryDao, service = service)

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(recipesRepository)
    val favoritesListViewModelFactory = FavoritesListViewModelFactory(recipesRepository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(recipesRepository)
    val recipeViewModelFactory = RecipeViewModelFactory(recipesRepository)
}