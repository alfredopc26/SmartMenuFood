package com.example.facilRecetas.persistence

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface FoodDao {
    @Query("SELECT * FROM food")
    fun getAllFood(): List<RecetteEntity>

    @Query("SELECT * FROM food WHERE _id = :id ")
    fun getFoodById(id: String): RecetteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: RecetteEntity)

    @Query("DELETE FROM food")
    fun deleteAllFood()
}