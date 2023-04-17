package com.example.smartmenufood.data.models

import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("_id")
    val id:String ,
    @SerializedName("strArea")
    val name: String)
