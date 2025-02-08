package com.knyazev.recipesapp.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity
data class Ingredient(
    @ColumnInfo val quantity: String,
    @ColumnInfo(name = "unit_of_measure") val unitOfMeasure: String,
    @ColumnInfo val description: String,
) : Parcelable
