package com.knyazev.recipesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.knyazev.recipesapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
}