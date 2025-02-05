package com.knyazev.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.knyazev.recipesapp.model.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM RECIPE")
    suspend fun getAllRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecipes(recipes: List<Recipe>)
}