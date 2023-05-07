package com.example.facilRecetas.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "strCategory") val strCategory: String,
    @ColumnInfo(name = "strCategoryThumb") val strCategoryThumb: String,
    @ColumnInfo(name = "strCategoryDescription") val strCategoryDescription: String
)
