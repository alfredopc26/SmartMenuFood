package com.example.facilRecetas.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "recette")
data class RecetteEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "likes") val likes: Int,
    @ColumnInfo(name = "dislikes") val dislikes: Int?,
    @ColumnInfo(name = "isBio") val isBio: Boolean,
    @ColumnInfo(name = "duration") val duration: Int?,
    @ColumnInfo(name = "person") val person: Int?,
    @ColumnInfo(name = "difficulty") val difficulty: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "comments") val comments: ArrayList<String>?,
    @ColumnInfo(name = "usersLiked") val usersLiked: ArrayList<String>,
    @ColumnInfo(name = "usersDisliked") val usersDisliked: ArrayList<String>,
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