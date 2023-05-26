package com.example.facilRecetas.ui.main.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils.attachBadgeDrawable
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Ingredients
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.ui.main.adapter.IngredientsAdapter
import com.example.facilRecetas.ui.main.view.IngredientsCartActivity
import com.example.facilRecetas.utils.services.Cart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BasketFragment : Fragment() {
    private lateinit var adapter: IngredientsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var ingredientsArrayList: ArrayList<String>

    private lateinit var badge : BadgeDrawable
    lateinit var toolbar: Toolbar
    lateinit var searchView: SearchView

    lateinit var noIngredientsLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noIngredientsLayout = view.findViewById(R.id.noIngredientBasketLayout)

        initIngredientsList()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.ingredientsBasketRV)
        recyclerView.layoutManager = layoutManager
        adapter = IngredientsAdapter(ingredientsArrayList)
        recyclerView.adapter = adapter

        noIngredientsLayout.visibility = if (ingredientsArrayList.isEmpty()) View.VISIBLE else View.GONE

        badge = BadgeDrawable.create(requireContext())
        badge.isVisible = true
        badge.number = Cart.cart.size

        adapter.setOnItemClickListener(object : IngredientsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val item = ingredientsArrayList[position]
                Cart.cart.add(item)
                ingredientsArrayList.remove(item)
                badge.number = Cart.cart.size
                Log.d("Cart", Cart.cart.toString())

                adapter.notifyDataSetChanged()
            }
        })

        toolbar = view.findViewById(R.id.ingredientsBasketToolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ingredients_cart -> {
                    val intent = Intent(context, IngredientsCartActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        searchView = toolbar.menu.findItem(R.id.ingredients_search).actionView as SearchView
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

        attachBadgeDrawable(badge, toolbar, R.id.ingredients_cart)

    }

    private fun filterList(newText: String) {
        val filteredList = ArrayList<String>()
        for (item in ingredientsArrayList) {
            if (item.toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item)
            }
        }

        if (filteredList.isEmpty()){
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("No Result")
                .setMessage("No food found with the keyword $newText")
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        } else {
            adapter.filterList(filteredList)
            adapter.setOnItemClickListener(object : IngredientsAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val item = filteredList[position]
                    Cart.cart.add(item)
                    filteredList.remove(item)
                    ingredientsArrayList.remove(item)
                    badge.number = Cart.cart.size
                    Log.d("Cart", Cart.cart.toString())
                    adapter.notifyDataSetChanged()
                }
            })


        }

    }


    private fun initIngredientsList() {
        ingredientsArrayList = ArrayList()
        val retIn = DatabaseFacilRecetas.getInstance(requireContext()).ingredientDao()
        val ingredients = retIn.getAllIngredient()

        if (ingredients.isNotEmpty()) {
            for (ingredient in ingredients!!) {
                ingredientsArrayList.add(ingredient.name)
            }
        }
        ingredientsArrayList.removeAll(Cart.cart)
        ingredientsArrayList.addAll(Cart.cartRemovedItems)

        if (ingredientsArrayList.isEmpty()) {
            noIngredientsLayout.visibility = View.VISIBLE
        } else {
            noIngredientsLayout.visibility = View.GONE
        }
    }
}