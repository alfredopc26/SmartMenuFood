package com.example.facilRecetas.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Area
import com.example.facilRecetas.data.models.Food
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.ui.main.adapter.AreaAdapter
import com.example.facilRecetas.ui.main.adapter.FoodAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryRecipesFilterActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: FoodAdapter
    lateinit var foodArrayList: ArrayList<Recette>
    lateinit var swiperRefreshLayout: SwipeRefreshLayout
    lateinit var areaList: ArrayList<String>
    lateinit var areaLisView: RecyclerView
    lateinit var areaAdapter: AreaAdapter
    lateinit var defaultArea: String
    lateinit var connectionLayout : LinearLayout
    lateinit var noDataLayout : LinearLayout
    private lateinit var dRecette: Recette


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_recipes_filter)
        if (intent.hasExtra("area")) {
            defaultArea = intent.getStringExtra("area").toString()
        }else{
            defaultArea = "All"
        }
        connectionLayout = findViewById(R.id.wifi_screen)
        noDataLayout = findViewById(R.id.recipes_not_found)

        toolbar = findViewById(R.id.foodFilterToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = defaultArea +" "+ intent.getStringExtra("category")+" Recipes"
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        initAreaList()
        initFoodList()
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.foodFilterList)
        recyclerView.layoutManager = layoutManager
        adapter = FoodAdapter(foodArrayList)
        recyclerView.adapter = adapter

        val areaLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        areaLisView = findViewById(R.id.areaFilterList)
        areaLisView.layoutManager = areaLayoutManager
        areaAdapter = AreaAdapter(areaList)
        areaLisView.adapter = areaAdapter

        areaAdapter.setOnItemClickListener(object : AreaAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                defaultArea = areaList[position]
                intent.putExtra("area", defaultArea)
                finish()
                startActivity(intent)
            }
        })

        adapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val food = foodArrayList[position]
                val intent = Intent(this@CategoryRecipesFilterActivity, FoodRecipeActivity::class.java)
                intent.putExtra("id", food.id)
                startActivity(intent)
            }
        })
    }
    private fun initFoodList() {
        val area = intent.getStringExtra("area")

        foodArrayList = ArrayList()
        val retIn = DatabaseFacilRecetas.getInstance(applicationContext).recetteDao()
        val recettes = retIn.getAllRecette()
        if (recettes.isNotEmpty()) {
            for (reccete in recettes!!) {
                dRecette = Recette(reccete._id,reccete.name,reccete.category,reccete.area,reccete.description, reccete.image, false, reccete.duration,reccete.person, reccete.difficulty, "", reccete.username,reccete.comments,reccete.likes,reccete.dislikes,reccete.usersLiked,reccete.usersDisliked,reccete.strIngredient1,reccete.strIngredient2,reccete.strIngredient3,reccete.strIngredient4,reccete.strIngredient5,reccete.strIngredient6,reccete.strIngredient7,reccete.strIngredient8,reccete.strIngredient9,reccete.strIngredient10,reccete.strMeasure1,reccete.strMeasure2,reccete.strMeasure3,reccete.strMeasure4,reccete.strMeasure5,reccete.strMeasure6,reccete.strMeasure7,reccete.strMeasure8, reccete.strMeasure9, reccete.strMeasure10)
                if (reccete.category == intent.getStringExtra("category")) {
                    if (area != null && area != "All") {
                        if (reccete.area == area) {
                            foodArrayList.add(dRecette)
                        }
                    } else {
                        foodArrayList.add(dRecette)
                    }
                }
            }
        }
    }


    private fun initAreaList() {
        areaList = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getAreasList()
        call.enqueue(object : Callback<List<Area>> {
            override fun onResponse(call: Call<List<Area>>, response: Response<List<Area>>) {
                if (response.isSuccessful) {
                    if (defaultArea != "All") {
                        areaList.add("All")
                    }
                    val areaNameList = response.body()
                    for (area in areaNameList!!) {
                        if(area.name != defaultArea){
                            areaList.add(area.name)
                        }
                    }
                    areaAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Area>>, t: Throwable) {
                Log.d("AreaList", "onFailure: ${t.message}")
            }
        })
    }
}