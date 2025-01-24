package com.knyazev.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.knyazev.recipesapp.Constants.REQUEST_URL
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.data.RecipeApiService
import com.knyazev.recipesapp.data.RecipesRepository
import com.knyazev.recipesapp.databinding.ActivityMainBinding
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for ActivityMainBinding must not be null")
//    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//        val client = OkHttpClient.Builder()
//            .addInterceptor(logger)
//            .build()

//        try {
//            threadPool.submit {


//                val requestCategory = Request.Builder()
//                    .url(REQUEST_URL)
//                    .build()
//                client.newCall(requestCategory).execute().use { response ->
//                    val deserializationString: String = response.body?.string() ?: ""
//
//                    Json.decodeFromString<List<Category>>(deserializationString.trimIndent())
//                        .map { category: Category -> category.id }
//                        .forEach { categoryId ->
//                            threadPool.submit {
//                                val requestRecipes = Request.Builder()
//                                    .url("https://recipes.androidsprint.ru/api/category/${categoryId}/recipes")
//                                    .build()
//                                client.newCall(requestRecipes).execute().use { response ->
//                                    val deserializationRecipes = response.body?.string() ?: ""
//                                    Json.decodeFromString<List<Recipe>>(deserializationRecipes.trimIndent())
//                                }
//                            }
//                        }
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("!!!", "Exception network ${e.message}")
//        } finally {
//            threadPool.shutdown()
//        }

        binding.binFavourites.setOnClickListener {

            findNavController(R.id.mainContainer).navigate(
                R.id.favoritesListFragment,
                null,
                navOptions {
                    launchSingleTop = true
                    anim {
                        enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                        exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                        popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                        popExit = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
                    }
                })
        }

        binding.binCategories.setOnClickListener {
            findNavController(R.id.mainContainer)
                .navigate(R.id.categoriesListFragment, null, navOptions {
                    launchSingleTop = true
                    anim {
                        enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                        exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                        popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                        popExit = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
                    }
                })
        }
    }
}