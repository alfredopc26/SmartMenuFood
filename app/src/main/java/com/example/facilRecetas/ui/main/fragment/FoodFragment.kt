package com.example.facilRecetas.ui.main.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.ui.main.adapter.FoodAdapter
import com.example.facilRecetas.data.models.Food
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.persistence.RecetteEntity
import com.example.facilRecetas.ui.main.view.FoodRecipeActivity
import com.example.facilRecetas.ui.main.view.RecetteFormActivity
import com.example.facilRecetas.utils.services.LoadingDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodFragment : Fragment() {
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var adapter : FoodAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var foodArrayList: ArrayList<Recette>
    private lateinit var searchView: SearchView
    private lateinit var noFoodLayout: LinearLayout
    private lateinit var dRecette: Recette
//    val loadingDialog = LoadingDialog(requireActivity())
    private lateinit var swiperRefreshLayout : SwipeRefreshLayout

    lateinit var addButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog(requireActivity())
        searchView = view.findViewById(R.id.foodIngredientSearchBar)
        searchView.clearFocus()


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        })


        swiperRefreshLayout = view.findViewById(R.id.foodIngredientSwipeRefresh)
        noFoodLayout = view.findViewById(R.id.noFoodLayout)
        initFoodList()
        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.foodListView)
        recyclerView.layoutManager = layoutManager
        adapter = FoodAdapter(foodArrayList)
        recyclerView.adapter = adapter

        noFoodLayout.visibility = if (foodArrayList.isEmpty()) View.VISIBLE else View.GONE





        swiperRefreshLayout.setOnRefreshListener {
            initFoodList()
            this.swiperRefreshLayout.isRefreshing = false
        }


        adapter.setOnItemClickListener(object : FoodAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, FoodRecipeActivity::class.java)
                intent.putExtra("id", foodArrayList[position].id)
                startActivity(intent)
            }
        })

        addButton= view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            val intent = Intent(context, RecetteFormActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterList(newText: String) {
        val filteredList = ArrayList<Recette>()
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
            AlertDialog.Builder(requireContext())
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

    private fun initFoodList(){
        loadingDialog.startLoadingDialog()
        foodArrayList = ArrayList()
        val ingredients = ArrayList<String>()
        foodArrayList.clear()
        val retIn = DatabaseFacilRecetas.getInstance(requireContext()).recetteDao()
        val recettes = retIn.getAllRecette()
        if (recettes.isNotEmpty()) {
            for (reccete in recettes!!) {
                dRecette = Recette(reccete._id,reccete.name,reccete.category,reccete.area,reccete.description, reccete.image, false, reccete.duration,reccete.person, reccete.difficulty, "", reccete.username,reccete.comments,reccete.likes,reccete.dislikes,reccete.usersLiked,reccete.usersDisliked,reccete.strIngredient1,reccete.strIngredient2,reccete.strIngredient3,reccete.strIngredient4,reccete.strIngredient5,reccete.strIngredient6,reccete.strIngredient7,reccete.strIngredient8,reccete.strIngredient9,reccete.strIngredient10,reccete.strMeasure1,reccete.strMeasure2,reccete.strMeasure3,reccete.strMeasure4,reccete.strMeasure5,reccete.strMeasure6,reccete.strMeasure7,reccete.strMeasure8, reccete.strMeasure9, reccete.strMeasure10)
                foodArrayList.add(dRecette)
            }
            loadingDialog.dismissDialog()
            if (foodArrayList.isEmpty()){
                noFoodLayout.visibility = View.VISIBLE
            } else {
                noFoodLayout.visibility = View.GONE
            }
        }

    }
}












//        val toolbar = view.findViewById<Toolbar>(R.id.foodSearchBar)
//        toolbar.menu.findItem(R.id.ingredientsCart).setOnMenuItemClickListener {
//            val intent = Intent(context, IngredientsCartActivity::class.java)
//            startActivity(intent)
//            true
//        }
//        toolbar.menu.findItem(R.id.favorite_food).setOnMenuItemClickListener {
//            val intent = Intent(context, FavoriteFoodActivity::class.java)
//            startActivity(intent)
//            true
//        }
//        toolbar.menu.findItem(R.id.favorite_food).setOnMenuItemClickListener {
//            Log.d("Cart", Cart.cart.toString())
//            true
//        }