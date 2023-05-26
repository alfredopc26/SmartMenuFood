package com.example.facilRecetas.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredient")
data class IngredientEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "strIngredient") val name: String,
    @ColumnInfo(name = "strDescription") val description: String,
)
