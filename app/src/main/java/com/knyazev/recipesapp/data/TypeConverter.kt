package com.knyazev.recipesapp.data

import androidx.room.TypeConverter
import com.knyazev.recipesapp.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeConverter {

    private val json = Json

    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientList(ingredientsList: String) : List<Ingredient> {
        return json.decodeFromString(ingredientsList)
    }
}