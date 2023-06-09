package com.example.facilRecetas.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facilRecetas.R
import com.example.facilRecetas.ui.main.adapter.IngredientCartAdapter
import com.example.facilRecetas.utils.services.Cart
import java.util.*

class IngredientsCartActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    private lateinit var adapter: IngredientCartAdapter
    lateinit var recyclerView: RecyclerView
    private lateinit var ingredientsArrayList: ArrayList<String>
    private lateinit var noIngredientsLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredients)
        noIngredientsLayout = findViewById(R.id.noIngredientCartLayout)

        initIngredientsList()
        val layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById(R.id.ingredientsRV)
        recyclerView.layoutManager = layoutManager
        adapter = IngredientCartAdapter(ingredientsArrayList)
        recyclerView.adapter = adapter


        adapter.setOnItemClickListener(object : IngredientCartAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Cart.cart.minus(ingredientsArrayList[position])
                Cart.cartRemovedItems.add(ingredientsArrayList[position])
                ingredientsArrayList.removeAt(position)

                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, ingredientsArrayList.size)
                if (ingredientsArrayList.size == 0) {
                    noIngredientsLayout.visibility = LinearLayout.VISIBLE
                }

            }
        })

        toolbar = findViewById(R.id.ingredientsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)



        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.ingredients_cart_menu, menu)

        val cartItem = menu!!.findItem(R.id.clear_cart)
        cartItem.setOnMenuItemClickListener { item ->
            if (Cart.cart.size >0){
            AlertDialog.Builder(this)
                .setTitle("Limpiar Canasta")
                .setMessage("¿Estas seguro de borrar tu canasta?")
                .setPositiveButton("Si") { dialog, which ->
                    Cart.cart.clear()
                    adapter.notifyDataSetChanged()
                    noIngredientsLayout.visibility = LinearLayout.VISIBLE
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()}
            else{
                AlertDialog.Builder(this)
                    .setTitle("Canasta vacía")
                    .setMessage("Tu canasta esta vacía")
                    .setPositiveButton("Ok") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
            true

        }

        val processIngredientsItem = menu!!.findItem(R.id.process_ingredients)
        processIngredientsItem.setOnMenuItemClickListener { item ->
            if (Cart.cart.size >0){
                processIngredientsItem.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.process_ingredients -> {
                            val intent = Intent(this, FoodByIngredientsActivity::class.java)
                            startActivity(intent)
                            true
                        }
                        else -> false
                    }
                }
            }
            else{
                AlertDialog.Builder(this)
                    .setTitle("Canasta vacía")
                    .setMessage("No hay items que procesar")
                    .setPositiveButton("Ok") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
            true

        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initIngredientsList(){
        ingredientsArrayList = ArrayList()
        if (Cart.cart.size > 0) {
            ingredientsArrayList = Cart.cart
            noIngredientsLayout.visibility = LinearLayout.GONE
        }
        else{
            noIngredientsLayout.visibility = LinearLayout.VISIBLE
        }
    }


    override fun onBackPressed() {

        if (Cart.cart.size == 0) {

            val intent = Intent(this, MainMenuActivity::class.java)
            intent.putExtra("openFragment", "BasketFragment")
            startActivity(intent)
        }else
            if (Cart.cartRemovedItems.size > 0) {
                val intent = Intent(this, MainMenuActivity::class.java)
                intent.putExtra("openFragment", "BasketFragment")
                startActivity(intent)
            }
        finish()
    }

}



//
//val item = searchArrayList[position]
//val itemView = recyclerView.findViewHolderForAdapterPosition(position)
//
//if (igredientCart.contains(item)) {
//
//    itemView!!.itemView.findViewById<ImageButton>(R.id.ingredientItemIcon)
//        .setImageResource(R.drawable.ic_baseline_add_24)
//    itemView!!.itemView.findViewById<TextView>(R.id.ingredient_name)
//        .setTextColor(resources.getColor(androidx.constraintlayout.widget.R.color.material_grey_600))
//
//} else {
//
//
//    itemView!!.itemView.findViewById<ImageButton>(R.id.ingredientItemIcon)
//        .setImageResource(R.drawable.ic_baseline_remove_24)
//    itemView!!.itemView.findViewById<TextView>(R.id.ingredient_name)
//        .setTextColor(resources.getColor(androidx.appcompat.R.color.material_grey_300))
//
//}
//Log.d("Cart", Cart.cart.toString())
//

