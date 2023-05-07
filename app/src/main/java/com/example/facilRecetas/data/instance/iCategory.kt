package com.example.facilRecetas.data.instance

import android.content.Context
import androidx.room.Room
import com.example.facilRecetas.persistence.CategoryDao
import com.example.facilRecetas.persistence.CategoryDatabase


class ICategory {
    companion object {
        private var sCategory: ICategory? = null
        private var mCategoryDao: CategoryDao? = null

        private fun iCategory(context: Context?) {
            val appContext = context?.applicationContext
            val database: CategoryDatabase? =
                appContext?.let {
                    Room.databaseBuilder(it, CategoryDatabase::class.java, "category")
                        .allowMainThreadQueries().build()
                };
            if (database != null) {
                mCategoryDao = database.categoryDao()
            };
        }

        fun getCategoryInstance(context: Context): ICategory? {
            return sCategory
        }

    }
}



