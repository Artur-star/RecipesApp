package com.knyazev.recipesapp.data

import androidx.room.TypeConverter
import com.knyazev.recipesapp.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TypeConverter {

    private val json = Json

    @TypeConverter
    fun fromIngredientList(ingredients: ArrayList<Ingredient>): String {
        return json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientList(ingredientsList: String): ArrayList<Ingredient> {
        return json.decodeFromString(ingredientsList)
    }

    @TypeConverter
    fun fromMethodList(method: ArrayList<String>): String {
        return json.encodeToString(method)
    }

    @TypeConverter
    fun toMethodList(method: String): ArrayList<String> {
        return json.decodeFromString(method)
    }


}