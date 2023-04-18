package com.example.smartmenufood.utils.services

import com.example.smartmenufood.data.models.Food

class Cart {
    companion object {
        var cart: ArrayList<String> = ArrayList()

        var cartRemovedItems: ArrayList<String> = ArrayList()
    }
}