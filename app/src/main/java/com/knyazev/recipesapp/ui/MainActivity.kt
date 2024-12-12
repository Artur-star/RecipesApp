package com.knyazev.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.knyazev.recipesapp.R
import com.knyazev.recipesapp.databinding.ActivityMainBinding
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for ActivityMainBinding must not be null")
    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread {
            val urlCategories = URL("https://recipes.androidsprint.ru/api/category")
            val connectionCategory = urlCategories.openConnection() as HttpURLConnection
            connectionCategory.connect()

            val deserializationString: String =
                connectionCategory.inputStream.bufferedReader().readLine()

            Log.i("!!!", "Выполняю запрос в потоке ${Thread.currentThread().name}")
            Log.i("!!!", "Body $deserializationString")

            val jsonCategory: List<Int> =
                Json.decodeFromString<List<Category>>(deserializationString.trimIndent())
                    .map { category: Category ->
                        category.id
                    }
            threadPool.submit {
                for (i in jsonCategory) {
                    val urlRecipes =
                        URL("https://recipes.androidsprint.ru/api/category/${i}/recipes")
                    val connectionRecipes = urlRecipes.openConnection() as HttpURLConnection
                    connectionRecipes.connect()

                    val deserializationRecipes =
                        connectionRecipes.inputStream.bufferedReader().readLine()
                    val jsonRecipes: List<Recipe> =
                        Json.decodeFromString<List<Recipe>>(deserializationRecipes.trimIndent())
                    Log.i("!!!", "Resipes: $jsonRecipes")
                }
            }
            threadPool.shutdown()

            Log.i("!!!", "Categories id: $jsonCategory")
        }.start()

        Log.i("!!!", "Метод onCreate() выполняется на потоке ${Thread.currentThread().name}")

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