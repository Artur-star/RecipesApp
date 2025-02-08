package com.knyazev.recipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo val title: String,
    val ingredients: ArrayList<Ingredient>,
    val method: ArrayList<String>,
    @ColumnInfo("image_url") val imageUrl: String,
    @ColumnInfo("category_id") val categoryId: Int = -1,
    @ColumnInfo("is_favorite") val isFavorite: Boolean = false,
) : Parcelable
