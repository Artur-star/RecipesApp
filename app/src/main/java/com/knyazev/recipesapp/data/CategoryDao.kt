package com.knyazev.recipesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.knyazev.recipesapp.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    suspend fun getAllCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategories(categories: List<Category>)
}