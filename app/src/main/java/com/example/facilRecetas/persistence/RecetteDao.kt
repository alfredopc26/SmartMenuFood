package com.example.facilRecetas.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.facilRecetas.data.models.Recette

@Dao
interface RecetteDao {

    @Query("SELECT * FROM recette")
    fun getAllRecette(): List<RecetteEntity>

    @Query("SELECT * FROM recette WHERE _id = :id ")
    fun getRecetteById(id: String): RecetteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecette(recette: RecetteEntity)

    @Query("DELETE FROM recette")
    fun deleteAllRecette()
}