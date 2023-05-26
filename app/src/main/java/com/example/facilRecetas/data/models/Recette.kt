package com.example.facilRecetas.data.models

import com.google.gson.annotations.SerializedName


data  class Recette (
    @SerializedName("_id")
    val  id :  String ,
    @SerializedName("name")
    val  name :  String ,
    @SerializedName("category")
    val  category :  String ,
    @SerializedName("area")
    val  area :  String ,
    @SerializedName("description")
    val  description :  String ,
    @SerializedName("image")
    val  image :  String ,
    @SerializedName("isBio")
    val  isBio :  Boolean,
    @SerializedName("duration")
    val  duration :  Int?,
    @SerializedName("person")
    val  person :  Int,
    @SerializedName("difficulty")
    val  difficulty :  String,
    @SerializedName("userId")
    val  user :  String,
    @SerializedName("username")
    val  username :  String,
    @SerializedName("comments")
    val  comments :  ArrayList<String>,
    @SerializedName("likes")
    val  likes :  Int,
    @SerializedName("dislikes")
    val  dislikes :  Int,
    @SerializedName("usersLiked")
    val  usersLiked :  ArrayList<String>,
    @SerializedName("usersDisliked")
    val  usersDisliked :  ArrayList<String>,
    @SerializedName("strIngredient1")
    val strIngredient1: String,
    @SerializedName("strIngredient2")
    val strIngredient2: String,
    @SerializedName("strIngredient3")
    val strIngredient3: String,
    @SerializedName("strIngredient4")
    val strIngredient4: String,
    @SerializedName("strIngredient5")
    val strIngredient5: String,
    @SerializedName("strIngredient6")
    val strIngredient6: String,
    @SerializedName("strIngredient7")
    val strIngredient7: String,
    @SerializedName("strIngredient8")
    val strIngredient8: String,
    @SerializedName("strIngredient9")
    val strIngredient9: String,
    @SerializedName("strIngredient10")
    val strIngredient10: String,
    @SerializedName("strMeasure1")
    val strMeasure1: String,
    @SerializedName("strMeasure2")
    val strMeasure2: String,
    @SerializedName("strMeasure3")
    val strMeasure3: String,
    @SerializedName("strMeasure4")
    val strMeasure4: String,
    @SerializedName("strMeasure5")
    val strMeasure5: String,
    @SerializedName("strMeasure6")
    val strMeasure6: String,
    @SerializedName("strMeasure7")
    val strMeasure7: String,
    @SerializedName("strMeasure8")
    val strMeasure8: String,
    @SerializedName("strMeasure9")
    val strMeasure9: String,
    @SerializedName("strMeasure10")
    val strMeasure10: String
    ){
        constructor(name: String, category: String, area: String, description: String,image: String,isBio:Boolean,duration:Int,person:Int,difficulty: String,user: String,username:String,strIngredient1: String,strIngredient2: String,strIngredient3: String,strIngredient4: String,strIngredient5: String,strIngredient6: String,strIngredient7: String,strIngredient8: String,strIngredient9: String,strIngredient10: String,strMeasure1: String,strMeasure2: String,strMeasure3: String,strMeasure4: String,strMeasure5: String,strMeasure6: String,strMeasure7: String,strMeasure8: String,strMeasure9: String,strMeasure10: String) :
                this("", name, category, area, description, image, isBio,duration,person,difficulty,user,username,ArrayList<String>(),0,0,ArrayList<String>(),ArrayList<String>(),strIngredient1,strIngredient2,strIngredient3,strIngredient4,strIngredient5,strIngredient6,strIngredient7,strIngredient8,strIngredient9,strIngredient10,strMeasure1,strMeasure2,strMeasure3,strMeasure4,strMeasure5,strMeasure6,strMeasure7,strMeasure8,strMeasure9,strMeasure10)

        constructor(name: String, category: String, area: String, description: String,image: String,isBio:Boolean,duration:Int,person:Int,difficulty: String,strIngredient1: String,strIngredient2: String,strIngredient3: String,strIngredient4: String,strIngredient5: String,strIngredient6: String,strIngredient7: String,strIngredient8: String,strIngredient9: String,strIngredient10: String,strMeasure1: String,strMeasure2: String,strMeasure3: String,strMeasure4: String,strMeasure5: String,strMeasure6: String,strMeasure7: String,strMeasure8: String,strMeasure9: String,strMeasure10: String) :
                this("", name, category, area, description, image, isBio,duration,person,difficulty,"","",ArrayList<String>(),0,0,ArrayList<String>(),ArrayList<String>(),strIngredient1,strIngredient2,strIngredient3,strIngredient4,strIngredient5,strIngredient6,strIngredient7,strIngredient8,strIngredient9,strIngredient10,strMeasure1,strMeasure2,strMeasure3,strMeasure4,strMeasure5,strMeasure6,strMeasure7,strMeasure8,strMeasure9,strMeasure10)
    }

