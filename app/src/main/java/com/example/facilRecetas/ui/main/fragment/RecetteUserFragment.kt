package com.example.facilRecetas.ui.main.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.ui.main.adapter.BlogAdapter
import com.example.facilRecetas.ui.main.view.RecetteActivity
import com.example.facilRecetas.utils.services.Cart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecetteUserFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var recyclerView : RecyclerView

    private lateinit var recetteArray: ArrayList<Recette>
    private lateinit var toolbar: Toolbar

    private lateinit var emptyRecipeLayout: LinearLayout
    private lateinit var searchView: SearchView

    private lateinit var recetteArrayList: java.util.ArrayList<Recette>
    private lateinit var blogAdapter: BlogAdapter





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recette_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emptyRecipeLayout = view.findViewById(R.id.noIngredientFilterFoodLayout)
        emptyRecipeLayout.visibility = LinearLayout.GONE

        toolbar = view.findViewById(R.id.foodIngredientSearchBar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        toolbar.setTitle("")
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener() {
            activity?.finish()

        }

        val layoutManager = LinearLayoutManager(context)
        getRecetteByIngredients()

        recyclerView = view.findViewById(R.id.blogList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        blogAdapter = BlogAdapter(recetteArrayList)
        recyclerView.adapter = blogAdapter


        blogAdapter.setOnItemClickListener(object : BlogAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, RecetteActivity::class.java)
                intent.putExtra("id", recetteArrayList[position].id)
                startActivity(intent)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_menu, menu)
        val search = menu.findItem(R.id.food_search)
        val searchView = search?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getRecetteByIngredients(){
        recetteArrayList = java.util.ArrayList()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getRecette()
        call.enqueue(object : Callback<List<Recette>> {
            override fun onResponse(call: Call<List<Recette>>, response: Response<List<Recette>>) {
                if (response.isSuccessful) {
                    val recetteList = response.body()
                    for (recette in recetteList!!) {
                        if (recetteFilter(recette)) {
                            recetteArrayList.add(recette)
                            Log.d("recetteArrayList",recetteArrayList.toString())

                        }
                    }
                    if (recetteArrayList.size == 0) {
                        emptyRecipeLayout.visibility = LinearLayout.VISIBLE
                    }
                    blogAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<Recette>>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    private fun recetteFilter(recette: Recette): Boolean {
        val ingredients = java.util.ArrayList<String>()
        if(!recette.strIngredient1.isNullOrEmpty() && recette.strIngredient1.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient1.toString()) }
        if(!recette.strIngredient2.isNullOrEmpty() && recette.strIngredient2.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient2.toString()) }
        if(!recette.strIngredient3.isNullOrEmpty() && recette.strIngredient3.toString().trim().isNotBlank()){ ingredients.add(recette?.strIngredient3.toString()) }
        if(!recette.strIngredient4.isNullOrEmpty() && recette.strIngredient4.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient4.toString()) }
        if(!recette.strIngredient5.isNullOrEmpty() && recette.strIngredient5.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient5.toString()) }
        if(!recette.strIngredient6.isNullOrEmpty() && recette.strIngredient6.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient6.toString()) }
        if(!recette.strIngredient7.isNullOrEmpty() && recette.strIngredient7.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient7.toString()) }
        if(!recette.strIngredient8.isNullOrEmpty() && recette.strIngredient8.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient8.toString()) }
        if(!recette.strIngredient9.isNullOrEmpty() && recette.strIngredient9.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient9.toString()) }
        if(!recette.strIngredient10.isNullOrEmpty() && recette.strIngredient10.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient10.toString()) }

        if (ingredients.containsAll(Cart.cart)) {
            Log.d("recette", ingredients.toString())
            return true
        }
        return false
    }

    private fun filterList(newText: String) {
        val filteredList = java.util.ArrayList<Recette>()
        for (item in recetteArrayList) {
            if (item.name.toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()){
            AlertDialog.Builder(context)
                .setTitle("No Result")
                .setMessage("No food found with the keyword $newText")
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        } else {
            blogAdapter.filterList(filteredList)
            blogAdapter.setOnItemClickListener(object : BlogAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(context, RecetteActivity::class.java)
                    intent.putExtra("id", filteredList[position].id)
                    startActivity(intent)
                }
            })


        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            filterList(newText)
        }
        return true
    }
}