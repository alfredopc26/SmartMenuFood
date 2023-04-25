
package com.example.facilRecetas.data.api

import com.example.facilRecetas.data.models.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RestApiService {

    @GET("recettes/{id}/comments")
    fun getCommentsByRecette(@Path("id")id:String):Call<List<Comment>>

    @POST("comments")
    fun postComment(
        @Body info: Comment
    ):Call<ResponseBody>

    //***********************Ingredient***********************//
    @GET("ingredients")
    fun getIngredientsList(): Call<List<Ingredients>>

    //***********************Category***********************//
    @GET("categories")
    fun getCategoriesList(): Call<List<Category>>
    //***********************Food***********************//
    @GET("food")
    fun getFoodsList(): Call<List<Food>>
    @GET("food/vegan")
    fun getFoodsVegan(): Call<List<Food>>
    @GET("food/vegetarian")
    fun getFoodsVegetarian(): Call<List<Food>>

    @GET("food/{id}")
    fun getFoodById(@Path("id") id: String): Call<Food>
    /*********************** Area ***********************/
    @GET("areas")
    fun getAreasList(): Call<List<Area>>

    @Multipart
    @POST("uploadfile")
    fun uploadImage(
        @Part myFile: MultipartBody.Part
    ): Call<ResponseBody>


    @GET("recettes")
    fun getRecette(): Call<List<Recette>>
    @Headers("Content-Type:application/json")
    @GET("recettes/{id}")
    fun getRecetteById(@Path("id") id: String): Call<Recette>

    @GET("recettes/{id}/recettes")
    fun getRecetteByUser(@Path("id")id:String): Call<List<Recette>>

    @Headers("Content-Type:application/json")
    @POST("recettes")
    fun addRecette(
        @Body info: Recette
    ): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @PATCH("recettes/{id}")
    fun updateRecette(@Path("id") id: String, @Body info: Recette): Call<ResponseBody>

    @DELETE("recettes/{id}")
    fun deleteRecette(
        @Path("id") id: String,
    ): Call<ResponseBody>

}

class RetrofitInstance {
    companion object {

//        const val BASE_URL: String = "http://10.0.2.2:3000/api/"
//        const val BASE_URL: String = "http://192.168.1.14:3000/api/"
//        const val BASE_URL: String = "https://easykitchenbackend.onrender.com/api/"
          const val BASE_URL: String = "http://192.168.1.4:3000/"
//        const val BASE_URL: String = "https://8e468e9d-d2db-464d-a954-92deff7d7a29.mock.pstmn.io/"

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

