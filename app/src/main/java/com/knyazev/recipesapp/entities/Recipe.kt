package com.knyazev.recipesapp.entities

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
)
