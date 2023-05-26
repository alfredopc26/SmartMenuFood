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
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
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
    private lateinit var foodList: ArrayList<Recette>
    private lateinit var recetteList: ArrayList<Recette>
    private lateinit var dCategory: Category
    private lateinit var dRecette: Recette
    private lateinit var db: DatabaseFacilRecetas

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
        if (categoryList.isNotEmpty()) {
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
        }

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
        val categories = DatabaseFacilRecetas.getInstance(requireContext()).categoryDao().getAllCategories()
        if (categories.size > 0) {
            for (cat in categories!!) {
                dCategory = Category(cat._id, cat.strCategory,cat.strCategoryDescription, cat.strCategoryThumb )
                categoryList.add(dCategory)
            }
        }
    }

    private fun initFoodList() {

        foodList = ArrayList()
        val retIn = DatabaseFacilRecetas.getInstance(requireContext()).recetteDao()
        val recettes = retIn.getAllRecette()
        if (recettes.isNotEmpty()) {
            for (reccete in recettes!!) {
                dRecette = Recette(reccete._id,reccete.name,reccete.category,reccete.area,reccete.description, reccete.image, false, reccete.duration,reccete.person, reccete.difficulty, "", reccete.username,reccete.comments,reccete.likes,reccete.dislikes,reccete.usersLiked,reccete.usersDisliked,reccete.strIngredient1,reccete.strIngredient2,reccete.strIngredient3,reccete.strIngredient4,reccete.strIngredient5,reccete.strIngredient6,reccete.strIngredient7,reccete.strIngredient8,reccete.strIngredient9,reccete.strIngredient10,reccete.strMeasure1,reccete.strMeasure2,reccete.strMeasure3,reccete.strMeasure4,reccete.strMeasure5,reccete.strMeasure6,reccete.strMeasure7,reccete.strMeasure8, reccete.strMeasure9, reccete.strMeasure10)
                foodList.add(dRecette)
            }

            val randomFood = foodList.random()
            foodList.clear()
            foodList.add(randomFood)
        }
    }

    private fun initRecetteList() {
        recetteList = ArrayList()
        recetteArray = ArrayList()
        val retIn = DatabaseFacilRecetas.getInstance(requireContext()).recetteDao()
        val recettes = retIn.getAllRecette()
        if (recettes.isNotEmpty()) {
            for (reccete in recettes!!) {
                dRecette = Recette(reccete._id,reccete.name,reccete.category,reccete.area,reccete.description, reccete.image, false, reccete.duration,reccete.person, reccete.difficulty, "", reccete.username,reccete.comments,reccete.likes,reccete.dislikes,reccete.usersLiked,reccete.usersDisliked,reccete.strIngredient1,reccete.strIngredient2,reccete.strIngredient3,reccete.strIngredient4,reccete.strIngredient5,reccete.strIngredient6,reccete.strIngredient7,reccete.strIngredient8,reccete.strIngredient9,reccete.strIngredient10,reccete.strMeasure1,reccete.strMeasure2,reccete.strMeasure3,reccete.strMeasure4,reccete.strMeasure5,reccete.strMeasure6,reccete.strMeasure7,reccete.strMeasure8, reccete.strMeasure9, reccete.strMeasure10)
                recetteArray.add(dRecette)
            }
        }
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