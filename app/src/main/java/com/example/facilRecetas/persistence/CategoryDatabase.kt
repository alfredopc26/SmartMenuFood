package com.example.facilRecetas.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CategoryEntity::class], version = 1, exportSchema = false)
abstract class CategoryDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {

        @Volatile private var INSTANCE: CategoryDatabase? = null

        fun getInstance(context: Context): CategoryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CategoryDatabase::class.java, "Category.db")
                .allowMainThreadQueries()
                .build()
    }

}