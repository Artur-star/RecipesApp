package com.knyazev.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.knyazev.recipesapp.model.Category
import com.knyazev.recipesapp.model.Recipe

@Database(entities = [Category::class, Recipe::class], version = 1)
@TypeConverters(value = [TypeConverter::class])
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao
}