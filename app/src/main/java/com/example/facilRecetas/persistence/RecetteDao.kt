package com.example.facilRecetas.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface RecetteDao {

    @Query("SELECT * FROM recette")
    fun getAllRecette(): List<RecetteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecette(recette: RecetteEntity)

    @Query("DELETE FROM recette")
    fun deleteAllRecette()
}