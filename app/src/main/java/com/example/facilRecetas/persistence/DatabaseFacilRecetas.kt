package com.example.facilRecetas.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [CategoryEntity::class, RecetteEntity::class], version = 1, exportSchema = false)
@TypeConverters(ArrayListConverter::class)
abstract class DatabaseFacilRecetas : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recetteDao(): RecetteDao

    companion object {

        @Volatile private var INSTANCE: DatabaseFacilRecetas? = null

        fun getInstance(context: Context): DatabaseFacilRecetas =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                DatabaseFacilRecetas::class.java, "FacilRecetas.db")
                .allowMainThreadQueries()
                .build()
    }

}