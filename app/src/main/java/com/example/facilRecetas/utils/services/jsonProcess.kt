package com.example.facilRecetas.utils.services

import android.content.Context
import com.example.facilRecetas.data.models.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class jsonProcess(private val Context: Context) {
    fun readJsonCategories(jsonFileName: String): List<Category> {
        val json = Context.assets.open(jsonFileName).bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<Context>>() {}.type
        return Gson().fromJson(json, listType)
    }
}