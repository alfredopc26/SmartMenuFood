package com.example.smartmenufood.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmenufood.R
import com.example.smartmenufood.data.api.RetrofitInstance
import com.example.smartmenufood.data.api.RestApiService
import com.example.smartmenufood.ui.main.view.CategoryRecipesFilterActivity
import com.example.smartmenufood.data.models.Category
import com.example.smartmenufood.data.models.Recette
import com.example.smartmenufood.ui.main.adapter.BlogAdapter
import com.example.smartmenufood.ui.main.adapter.CategoryAdapter
import com.example.smartmenufood.ui.main.view.MainMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private lateinit var categoryRecyclerView: RecyclerView
private lateinit var categoryAdapter: CategoryAdapter

class HomeFragment : Fragment() {
    //************* Recommended food *************//
    private lateinit var recommendedFoodRecyclerView: RecyclerView
    private lateinit var recommendedFoodAdapter: BlogAdapter

    private lateinit var recetteArray:ArrayList<Recette>
    private lateinit var recetteArrayHeader: TextView
    private lateinit var categoryList: ArrayList<Category>

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
        val navDrawerButton = view.findViewById<Button>(R.id.menu)
        username= view.findViewById(R.id.title_username)

        val drawerLayout = (activity as MainMenuActivity).drawerLayout
        navDrawerButton.setOnClickListener {
            drawerLayout.open()
        }
        username.text="admin"

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

}