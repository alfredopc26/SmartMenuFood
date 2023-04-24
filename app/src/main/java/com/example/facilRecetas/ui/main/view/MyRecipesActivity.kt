package com.example.facilRecetas.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.ui.main.adapter.BlogAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRecipesActivity : AppCompatActivity() {
    private lateinit var adapter : BlogAdapter
    private lateinit var recyclerView : RecyclerView

    lateinit var formButton: FloatingActionButton

    private lateinit var recetteArray: ArrayList<Recette>
    private lateinit var usernameArray: ArrayList<String>

    lateinit var idUser : String
    private lateinit var idRecette : String
    lateinit var user : HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)


        initBlogList()
        val layoutManager = LinearLayoutManager(this)
        recyclerView =findViewById(R.id.blogList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        adapter = BlogAdapter(recetteArray)
        recyclerView.adapter = adapter


        adapter.setOnItemClickListener(object : BlogAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MyRecipesActivity, MySingleRecipeActivity::class.java)
                intent.putExtra("id", recetteArray[position].id)
                startActivity(intent)
            }
        })

    }



    private fun initBlogList() {
        recetteArray = ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getRecetteByUser(idUser)
        call.enqueue(object : Callback<List<Recette>> {
            override fun onResponse(call: Call<List<Recette>>, response: Response<List<Recette>>) {
                if(response.body() != null) {

//                    recetteArray.addAll(response.body()!!)
                    val listRecette = response.body()!!.sortedWith(compareBy { (it.likes.toFloat()-it.dislikes.toFloat()) })
                    recetteArray.addAll(listRecette)
                    adapter.notifyDataSetChanged()

                    Log.d("recetteArray",recetteArray.size.toString())

                }


            }

            override fun onFailure(call: Call<List<Recette>>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }

        })
    }
}