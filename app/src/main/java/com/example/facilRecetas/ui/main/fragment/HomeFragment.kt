package com.example.facilRecetas.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.ui.main.view.CategoryRecipesFilterActivity
import com.example.facilRecetas.data.models.Category
import com.example.facilRecetas.data.models.Food
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.ui.main.adapter.BlogAdapter
import com.example.facilRecetas.ui.main.adapter.CategoryAdapter
import com.example.facilRecetas.ui.main.adapter.FoodAdapter
import com.example.facilRecetas.ui.main.view.FoodRecipeActivity
import com.example.facilRecetas.ui.main.view.MainMenuActivity
import com.example.facilRecetas.ui.main.view.RecetteActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime

private lateinit var categoryRecyclerView: RecyclerView
private lateinit var categoryAdapter: CategoryAdapter

private lateinit var expertRecipesRecyclerView: RecyclerView
private lateinit var expertRecipesAdapter: FoodAdapter

class HomeFragment : Fragment() {
    //************* Recommended food *************//
    private lateinit var recommendedFoodRecyclerView: RecyclerView
    private lateinit var recommendedFoodAdapter: BlogAdapter

    private lateinit var recetteArray:ArrayList<Recette>
    private lateinit var recetteArrayHeader: TextView
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var foodList: ArrayList<Food>

    private lateinit var username: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drawerLayout = (activity as MainMenuActivity).drawerLayout

        initCategoryList()
        val categoryLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView = view.findViewById(R.id.categorieView)
        categoryRecyclerView.layoutManager = categoryLayoutManager
        categoryRecyclerView.setHasFixedSize(true)
        categoryAdapter = CategoryAdapter(categoryList)
        categoryRecyclerView.adapter = categoryAdapter

        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, CategoryRecipesFilterActivity::class.java)
                intent.putExtra("category", categoryList[position].name)
                startActivity(intent)
            }
        })

        initFoodList()
        val expertRecipesTextHeader = view.findViewById<TextView>(R.id.expertText)
        ("Recetas para " + dailyFood()).also { expertRecipesTextHeader.text = it }
        val expertRecipesLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        expertRecipesRecyclerView = view.findViewById(R.id.expertView)
        expertRecipesRecyclerView.layoutManager = expertRecipesLayoutManager
        expertRecipesRecyclerView.setHasFixedSize(true)
        expertRecipesAdapter = FoodAdapter(foodList)
        expertRecipesRecyclerView.adapter = expertRecipesAdapter
        expertRecipesAdapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, FoodRecipeActivity::class.java)
                intent.putExtra("id", foodList[position].id)
                startActivity(intent)
            }
        })

        initRecetteList()
        val recommendedLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recommendedFoodRecyclerView = view.findViewById(R.id.recommendedView)
        recommendedFoodRecyclerView.layoutManager = recommendedLayoutManager
        recommendedFoodAdapter = BlogAdapter(recetteArray)
        recommendedFoodRecyclerView.adapter = recommendedFoodAdapter

        recommendedFoodAdapter.setOnItemClickListener(object : BlogAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, RecetteActivity::class.java)
                intent.putExtra("id", recetteArray[position].id)
                startActivity(intent)
            }
        })


    }

    private fun initCategoryList() {
        categoryList = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getCategoriesList()
        call.enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                if (response.isSuccessful) {
                    val categories = response.body()
                    for (category in categories!!) {
                        categoryList.add(category)
                    }
                }
                categoryAdapter.notifyDataSetChanged()
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.d("400","Failure = "+t.toString());
            }

        })
    }

    private fun initFoodList() {
        foodList = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getFoodsList()
        call.enqueue((object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                if (response.isSuccessful) {
                    var foods = response.body()
                    for (food in foods!!) {
                        if (dailyFood()=="Desayunos"){
                            if (food.category=="Breakfast"){ foodList.add(food)}
                        }
                        if (dailyFood()=="Almuerzos"){
                            if (food.category=="Lamb" || food.category=="Pasta" || food.category=="Beef"){ foodList.add(food)}
                        }
                        if (dailyFood()=="Cenas"){
                            if (food.category=="Side" ||food.category=="Starter"){ foodList.add(food)}
                        }
                    }
                    val randomFood = foodList.random()
                    foodList.clear()
                    foodList.add(randomFood)
                    expertRecipesAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                Log.d("400","Failure = "+t.toString());
            }

        }))
    }

    private fun initRecetteList() {
        recetteArray = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getRecette()
        call.enqueue(object : Callback<List<Recette>> {
            override fun onResponse(call: Call<List<Recette>>, response: Response<List<Recette>>) {
                if(response.body() != null) {
//                    recetteArray.addAll(response.body()!!)
                    val listRecette = response.body()!!.sortedWith(compareByDescending  { (it.likes.toFloat()-it.dislikes.toFloat()) })
                    recetteArray.addAll(listRecette)
                    recommendedFoodAdapter.notifyDataSetChanged()
                    Log.d("recetteArray",recetteArray.size.toString())
                }
            }
            override fun onFailure(call: Call<List<Recette>>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }

        })
    }
    private fun dailyFood (): String {
        val current = LocalTime.now()
        val morning = LocalTime.of(6,0)
        val afternoon = LocalTime.of(12,0)
        val evening = LocalTime.of(18,0)
        if (current.isAfter(morning) && current.isBefore(afternoon)){ return "Desayunos"}
        else if (current.isAfter(afternoon) && current.isBefore(evening)){ return "Almuerzos"}
        return "Cenas"

    }
}