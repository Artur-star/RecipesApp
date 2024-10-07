package com.knyazev.recipesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.knyazev.recipesapp.databinding.ActivityMainBinding
import com.knyazev.recipesapp.fragments.CategoriesListFragment
import com.knyazev.recipesapp.fragments.FavoritesFragment

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding
            ?: throw IllegalArgumentException("Binding for ActivityMainBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("!!!", "onCreate in Main activity")
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            Log.d("!!!", "in if in Main activity")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(R.id.mainContainer)
            }
        }
        binding.binFavourites.setOnClickListener {
            Log.d("!!!", "binFavorite in Main activity")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<FavoritesFragment>(R.id.mainContainer)
                addToBackStack(null)
            }
        }
        binding.binCategories.setOnClickListener {
            Log.d("!!!", "binCategory in Main activity")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace<CategoriesListFragment>(R.id.mainContainer)
                addToBackStack(null)
            }
        }
    }
}