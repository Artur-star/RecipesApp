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
    @ColumnInfo val ingredients: List<Ingredient>,
    @ColumnInfo val method: List<String>,
    @ColumnInfo("image_url") val imageUrl: String,
) : Parcelable
