package com.example.facilRecetas.ui.main.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Food
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.ui.main.adapter.FoodAdapter
import com.example.facilRecetas.ui.main.view.FoodRecipeActivity
import com.example.facilRecetas.utils.services.Cart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExpertRecipeFragment : Fragment(), SearchView.OnQueryTextListener {


    lateinit var formButton: FloatingActionButton

    private lateinit var recetteArray: ArrayList<Recette>
    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private lateinit var foodArrayList: java.util.ArrayList<Recette>
    private lateinit var emptyRecipeLayout: LinearLayout
    private lateinit var searchView: SearchView
    private lateinit var dRecette: Recette


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expert_recipe, container, false)
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

        getFoodByIngredients()
        val layoutManager = LinearLayoutManager(context)

        recyclerView = view.findViewById(R.id.foodList)
        recyclerView.layoutManager = layoutManager
        adapter = FoodAdapter(foodArrayList)
        Log.d("FoodByIngredients", foodArrayList.toString())
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, FoodRecipeActivity::class.java)
                intent.putExtra("id", foodArrayList[position].id)
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

    private fun getFoodByIngredients(){
        foodArrayList = java.util.ArrayList()
        val retIn = DatabaseFacilRecetas.getInstance(requireContext()).recetteDao()
        val recettes = retIn.getAllRecette()
        if (recettes.isNotEmpty()) {
            for (reccete in recettes!!) {
                dRecette = Recette(reccete._id,reccete.name,reccete.category,reccete.area,reccete.description, reccete.image, false, reccete.duration,reccete.person, reccete.difficulty, "", reccete.username,reccete.comments,reccete.likes,reccete.dislikes,reccete.usersLiked,reccete.usersDisliked,reccete.strIngredient1,reccete.strIngredient2,reccete.strIngredient3,reccete.strIngredient4,reccete.strIngredient5,reccete.strIngredient6,reccete.strIngredient7,reccete.strIngredient8,reccete.strIngredient9,reccete.strIngredient10,reccete.strMeasure1,reccete.strMeasure2,reccete.strMeasure3,reccete.strMeasure4,reccete.strMeasure5,reccete.strMeasure6,reccete.strMeasure7,reccete.strMeasure8, reccete.strMeasure9, reccete.strMeasure10)
                if (foodFilter(dRecette) == true) {
                    Log.d("FoodFilter", foodFilter(dRecette).toString())
                    foodArrayList.add(dRecette)
                }
            }
            if (foodArrayList.size == 0) {
                emptyRecipeLayout.visibility = LinearLayout.VISIBLE
            }
        }
    }

    private fun foodFilter(food: Recette): Boolean {
        val ingredients = java.util.ArrayList<String>()
        if(!food.strIngredient1.isNullOrEmpty() && food.strIngredient1.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient1.toString()) }
        if(!food.strIngredient2.isNullOrEmpty() && food.strIngredient2.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient2.toString()) }
        if(!food.strIngredient3.isNullOrEmpty() && food.strIngredient3.toString().trim().isNotBlank()){ ingredients.add(food?.strIngredient3.toString()) }
        if(!food.strIngredient4.isNullOrEmpty() && food.strIngredient4.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient4.toString()) }
        if(!food.strIngredient5.isNullOrEmpty() && food.strIngredient5.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient5.toString()) }
        if(!food.strIngredient6.isNullOrEmpty() && food.strIngredient6.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient6.toString()) }
        if(!food.strIngredient7.isNullOrEmpty() && food.strIngredient7.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient7.toString()) }
        if(!food.strIngredient8.isNullOrEmpty() && food?.strIngredient8.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient8.toString()) }
        if(!food.strIngredient9.isNullOrEmpty() && food.strIngredient9.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient9.toString()) }
        if(!food.strIngredient10.isNullOrEmpty() && food.strIngredient10.toString().trim().isNotBlank()) { ingredients.add(food?.strIngredient10.toString()) }
        if (ingredients.containsAll(Cart.cart)) {
            Log.d("Food", Cart.cart.toString())
            return true
        }
        return false
    }


    private fun filterList(newText: String) {
        val filteredList = java.util.ArrayList<Recette>()
        for (item in foodArrayList) {
            if (item.name.toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item)
            }
            else if (item.area.toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item)
            }
            else if (item.category.toLowerCase().contains(newText.toLowerCase())) {
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
            adapter.filterList(filteredList)
            adapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(context, FoodRecipeActivity::class.java)
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
