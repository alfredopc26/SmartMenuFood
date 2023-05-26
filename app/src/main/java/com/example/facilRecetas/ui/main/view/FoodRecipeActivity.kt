package com.example.facilRecetas.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Food
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.ui.main.adapter.IngredientsTextAdapter
import com.example.facilRecetas.ui.main.adapter.MeasuresTextAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRecipeActivity : AppCompatActivity() {
    lateinit var id : String
    lateinit var ingredientsList : ArrayList<String>
    lateinit var mesuresList : ArrayList<String>

    lateinit var ingredientsRecyclerView: RecyclerView
    lateinit var mesuresRecyclerView: RecyclerView

    lateinit var ingredientsAdapter: IngredientsTextAdapter
    lateinit var measuresAdapter: MeasuresTextAdapter

    lateinit var webView: WebView
    lateinit var recipeLink : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_recipe)

        id = intent.getStringExtra("id").toString()
        val recipeImage = findViewById<ImageView>(R.id.recipeFoodImage)
        val recipeCategory = findViewById<TextView>(R.id.foodRecipeCategory)
        val recipeName = findViewById<TextView>(R.id.foodRecipeName)
        val recipeInstructions = findViewById<TextView>(R.id.foodRecipeInstructions)

        ingredientsList = ArrayList()
        mesuresList = ArrayList()





        val ingredientsLayoutManager = LinearLayoutManager(this)
        ingredientsRecyclerView = findViewById(R.id.ingredientListView)
        ingredientsRecyclerView.layoutManager = ingredientsLayoutManager

        val mesuresLayoutManager = LinearLayoutManager(this)
        mesuresRecyclerView = findViewById(R.id.mesureListView)
        mesuresRecyclerView.layoutManager = mesuresLayoutManager

        getRecipe(id, recipeImage, recipeCategory, recipeName, recipeInstructions)
        val toolbar = findViewById<Toolbar>(R.id.foodRecipeToolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()

        }

        webView = findViewById(R.id.foodRecipeSourceWebView)
        val source = findViewById<TextView>(R.id.foodRecipeSource)
        source.visibility = TextView.GONE
        webView.settings.javaScriptEnabled = true
        webView.settings.safeBrowsingEnabled = true


        source.setOnClickListener(){
            Log.d("recipeLink", recipeLink)
            if (!recipeLink.isNullOrBlank()){
                webView.loadUrl(recipeLink)
            } else {
                Toast.makeText(this, "No source available", Toast.LENGTH_SHORT).show()
            }


        }
    }

    fun getRecipe(id: String, recipeImage: ImageView, recipeCategory: TextView, recipeName: TextView, recipeInstructions: TextView) {
        val ingredients = ArrayList<String>()
        val measures = ArrayList<String>()
        val retIn = DatabaseFacilRecetas.getInstance(applicationContext).recetteDao()
        val food = retIn.getRecetteById(id)

        if(food._id != "") {
            recipeName.text = food?.name
            recipeCategory.text = food?.category
            recipeInstructions.text = food?.description
            Picasso.get().load(food?.image).into(recipeImage)
            if(!food?.strIngredient1.isNullOrEmpty() && food?.strIngredient1.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient1.toString()) }
            if(!food?.strIngredient2.isNullOrEmpty() && food?.strIngredient2.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient2.toString()) }
            if(!food?.strIngredient3.isNullOrEmpty() && food?.strIngredient3.toString().trim().isNotBlank()){ ingredients.add(food?.strIngredient3.toString()) }
            if(!food?.strIngredient4.isNullOrEmpty() && food?.strIngredient4.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient4.toString()) }
            if(!food?.strIngredient5.isNullOrEmpty() && food?.strIngredient5.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient5.toString()) }
            if(!food?.strIngredient6.isNullOrEmpty() && food?.strIngredient6.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient6.toString()) }
            if(!food?.strIngredient7.isNullOrEmpty() && food?.strIngredient7.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient7.toString()) }
            if(!food?.strIngredient8.isNullOrEmpty() && food?.strIngredient8.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient8.toString()) }
            if(!food?.strIngredient9.isNullOrEmpty() && food?.strIngredient9.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient9.toString()) }
            if(!food?.strIngredient10.isNullOrEmpty() && food?.strIngredient10.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient10.toString()) }

            ingredientsList.addAll(ingredients.filter { it.trim().isNotEmpty() })
            ingredientsAdapter = IngredientsTextAdapter(ingredientsList)
            ingredientsRecyclerView.adapter = ingredientsAdapter

            if (!food?.strMeasure1.isNullOrEmpty() && food?.strMeasure1.toString().trim().isNotBlank()) { measures.add(food?.strMeasure1.toString()) }
            if (!food?.strMeasure2.isNullOrEmpty() && food?.strMeasure2.toString().trim().isNotBlank()) { measures.add(food?.strMeasure2.toString()) }
            if (!food?.strMeasure3.isNullOrEmpty() && food?.strMeasure3.toString().trim().isNotBlank()) { measures.add(food?.strMeasure3.toString()) }
            if (!food?.strMeasure4.isNullOrEmpty() && food?.strMeasure4.toString().trim().isNotBlank()) { measures.add(food?.strMeasure4.toString()) }
            if (!food?.strMeasure5.isNullOrEmpty() && food?.strMeasure5.toString().trim().isNotBlank()) { measures.add(food?.strMeasure5.toString()) }
            if (!food?.strMeasure6.isNullOrEmpty() && food?.strMeasure6.toString().trim().isNotBlank()) { measures.add(food?.strMeasure6.toString()) }
            if (!food?.strMeasure7.isNullOrEmpty() && food?.strMeasure7.toString().trim().isNotBlank()) { measures.add(food?.strMeasure7.toString()) }
            if (!food?.strMeasure8.isNullOrEmpty() && food?.strMeasure8.toString().trim().isNotBlank()) { measures.add(food?.strMeasure8.toString()) }
            if (!food?.strMeasure9.isNullOrEmpty() && food?.strMeasure9.toString().trim().isNotBlank()) { measures.add(food?.strMeasure9.toString()) }
            if (!food?.strMeasure10.isNullOrEmpty() && food?.strMeasure10.toString().trim().isNotBlank()) { measures.add(food?.strMeasure10.toString()) }
            mesuresList.addAll(measures.filter { it.trim().isNotEmpty() })

            measuresAdapter = MeasuresTextAdapter(mesuresList)
            mesuresRecyclerView.adapter = measuresAdapter
            Log.d("mesureList", mesuresList.toString())
            Log.d("mesures", measures.toString())
        }
    }

}