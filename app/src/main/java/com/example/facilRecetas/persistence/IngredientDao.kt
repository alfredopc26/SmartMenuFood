package com.example.facilRecetas.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredient")
    fun getAllIngredient(): List<IngredientEntity>

    @Query("SELECT * FROM ingredient WHERE _id = :id ")
    fun getIngredientById(id: String): IngredientEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredient(ingredient: IngredientEntity)

    @Query("DELETE FROM ingredient")
    fun deleteAllIngredient()
}