package com.example.facilRecetas.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "strMeal") val name: String,
    @ColumnInfo(name = "strMealThumb") val description: String,
    @ColumnInfo(name = "strSource") val image: String,
    @ColumnInfo(name = "strYoutube") val likes: Int,
    @ColumnInfo(name = "strArea") val dislikes: Int,
    @ColumnInfo(name = "strCategory") val isBio: Boolean,
    @ColumnInfo(name = "strIngredient1") val strIngredient1: String,
    @ColumnInfo(name = "strIngredient2") val strIngredient2: String,
    @ColumnInfo(name = "strIngredient3") val strIngredient3: String,
    @ColumnInfo(name = "strIngredient4") val strIngredient4: String,
    @ColumnInfo(name = "strIngredient5") val strIngredient5: String,
    @ColumnInfo(name = "strIngredient6") val strIngredient6: String,
    @ColumnInfo(name = "strIngredient7") val strIngredient7: String,
    @ColumnInfo(name = "strIngredient8") val strIngredient8: String,
    @ColumnInfo(name = "strIngredient9") val strIngredient9: String,
    @ColumnInfo(name = "strIngredient10") val strIngredient10: String,
    @ColumnInfo(name = "strMeasure1") val strMeasure1: String,
    @ColumnInfo(name = "strMeasure2") val strMeasure2: String,
    @ColumnInfo(name = "strMeasure3") val strMeasure3: String,
    @ColumnInfo(name = "strMeasure4") val strMeasure4: String,
    @ColumnInfo(name = "strMeasure5") val strMeasure5: String,
    @ColumnInfo(name = "strMeasure6") val strMeasure6: String,
    @ColumnInfo(name = "strMeasure7") val strMeasure7: String,
    @ColumnInfo(name = "strMeasure8") val strMeasure8: String,
    @ColumnInfo(name = "strMeasure9") val strMeasure9: String,
    @ColumnInfo(name = "strMeasure10") val strMeasure10: String
)