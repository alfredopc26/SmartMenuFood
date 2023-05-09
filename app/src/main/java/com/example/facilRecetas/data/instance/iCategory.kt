package com.example.facilRecetas.data.instance

import android.content.Context
import androidx.room.Room
import com.example.facilRecetas.persistence.CategoryDao
import com.example.facilRecetas.persistence.DatabaseFacilRecetas


class ICategory {
    companion object {
        private var sCategory: ICategory? = null
        private var mCategoryDao: CategoryDao? = null

        private fun iCategory(context: Context?) {
            val appContext = context?.applicationContext
            val database: DatabaseFacilRecetas? =
                appContext?.let {
                    Room.databaseBuilder(it, DatabaseFacilRecetas::class.java, "category")
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



