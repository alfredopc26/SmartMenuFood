package com.example.facilRecetas.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Comment
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.ui.main.adapter.CommentAdapter
import com.example.facilRecetas.ui.main.adapter.IngredientsRecetteTextAdapter

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySingleRecipeActivity : AppCompatActivity() {

    private lateinit var recyclerViewIngredient: RecyclerView
    private lateinit var adapterIngredient: IngredientsRecetteTextAdapter
    private lateinit var adapter : CommentAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var recetteTitre: TextView
    private lateinit var timeTxt: TextView
    private lateinit var peopleTxt: TextView
    private lateinit var difficultyTxt: TextView
    private lateinit var instructionTxt: TextView
    private lateinit var recetteUser:TextView
    private lateinit var recetteLikes:TextView
    private lateinit var edit:AppCompatImageButton
    private lateinit var delete:AppCompatImageButton
    lateinit var commentInput:EditText
    //    lateinit var recette: Recette
    lateinit var idUser : String
    lateinit var username : String

    lateinit var idRecette : String
    lateinit var submitComment:AppCompatButton
    lateinit var user : HashMap<String, String>
    //    lateinit var userName:String
    lateinit var commentsList:ArrayList<Comment>

    lateinit var ingredientsList : ArrayList<String>
    lateinit var mesuresList : ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_single_recipe)

        idRecette = intent.getStringExtra("id").toString()
        Log.d("IDRECETTE",idRecette)
        idUser = "alfrepc"
        username = "Admin"
        ingredientsList = ArrayList()
        mesuresList = ArrayList()
        initRecette()

        val layoutManagerIngredient = LinearLayoutManager(this)

        recyclerViewIngredient = findViewById(R.id.ingredientListRecetteView)

        recyclerViewIngredient.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewIngredient.setHasFixedSize(true)
        adapterIngredient = IngredientsRecetteTextAdapter(ingredientsList, mesuresList)
        recyclerViewIngredient.adapter = adapterIngredient





        val layoutManager = LinearLayoutManager(this)

        recyclerView = findViewById(R.id.commentSession)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        adapter = CommentAdapter(commentsList,idUser)
        recyclerView.adapter = adapter

        recetteTitre = findViewById(R.id.foodRecipeName)
        timeTxt = findViewById(R.id.timeTxt)
        peopleTxt = findViewById(R.id.peopleTxt)
        difficultyTxt = findViewById(R.id.difficultyTxt)
        instructionTxt = findViewById(R.id.foodRecipeInstructions)
        recetteUser = findViewById(R.id.foodRecipeCategory)
        edit = findViewById(R.id.editRecette)
        delete = findViewById(R.id.deleteRecette)
        recetteLikes=findViewById(R.id.recetteLikes)


//        submitComment.setOnClickListener {
//            if (commentInput.text.isEmpty()){
//                commentInput.error = "comment is required"
//                commentInput.requestFocus()
//            }else{
//                postComment(commentInput.text.toString())
//                commentInput.text.clear()
//                reloadActivity()
//            }
//
//        }

        delete.setOnClickListener {
            AlertDialog.Builder(this@MySingleRecipeActivity)
                .setTitle("Are you sure?")
                .setMessage("You want to Delete this recipe ?")
                .setPositiveButton("Yes") { dialog, which ->
                    deleteRecette(idRecette)
                    val intent = Intent(this, MyRecipesActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .show()

            Log.d("clicked","clicked!")
        }
        edit.setOnClickListener {
            val intent = Intent(this, RecetteFormEditActivity::class.java)
            intent.putExtra("id", idRecette)
            startActivity(intent)
        }

    }
    private fun initRecette() {
        val ingredients = ArrayList<String>()
        val measures = ArrayList<String>()

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.getRecetteById(idRecette)
        call.enqueue(object : Callback<Recette> {
            override fun onResponse(
                call: Call<Recette>,
                response: Response<Recette>
            ) {
                val recette = response.body()

                recetteTitre.text = response.body()!!.name
                timeTxt.text = response.body()!!.duration.toString()
                peopleTxt.text = response.body()!!.person.toString()
                difficultyTxt.text = response.body()!!.difficulty
                instructionTxt.text =response.body()!!.description
                recetteUser.text = response.body()!!.username
                recetteLikes.text = (response.body()!!.likes.toFloat() - response.body()!!.dislikes.toFloat()).toInt().toString()

                if(!recette?.strIngredient1.isNullOrEmpty() && recette?.strIngredient1.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient1.toString()) }
                if(!recette?.strIngredient2.isNullOrEmpty() && recette?.strIngredient2.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient2.toString()) }
                if(!recette?.strIngredient3.isNullOrEmpty() && recette?.strIngredient3.toString().trim().isNotBlank()){ ingredients.add(recette?.strIngredient3.toString()) }
                if(!recette?.strIngredient4.isNullOrEmpty() && recette?.strIngredient4.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient4.toString()) }
                if(!recette?.strIngredient5.isNullOrEmpty() && recette?.strIngredient5.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient5.toString()) }
                if(!recette?.strIngredient6.isNullOrEmpty() && recette?.strIngredient6.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient6.toString()) }
                if(!recette?.strIngredient7.isNullOrEmpty() && recette?.strIngredient7.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient7.toString()) }
                if(!recette?.strIngredient8.isNullOrEmpty() && recette?.strIngredient8.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient8.toString()) }
                if(!recette?.strIngredient9.isNullOrEmpty() && recette?.strIngredient9.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient9.toString()) }
                if(!recette?.strIngredient10.isNullOrEmpty() && recette?.strIngredient10.toString().trim().isNotBlank()) { ingredients.add(recette?.strIngredient10.toString()) }

                ingredientsList.addAll(ingredients.filter { it.trim().isNotEmpty() })

                if (!recette?.strMeasure1.isNullOrEmpty() && recette?.strMeasure1.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure1.toString()) }
                if (!recette?.strMeasure2.isNullOrEmpty() && recette?.strMeasure2.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure2.toString()) }
                if (!recette?.strMeasure3.isNullOrEmpty() && recette?.strMeasure3.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure3.toString()) }
                if (!recette?.strMeasure4.isNullOrEmpty() && recette?.strMeasure4.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure4.toString()) }
                if (!recette?.strMeasure5.isNullOrEmpty() && recette?.strMeasure5.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure5.toString()) }
                if (!recette?.strMeasure6.isNullOrEmpty() && recette?.strMeasure6.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure6.toString()) }
                if (!recette?.strMeasure7.isNullOrEmpty() && recette?.strMeasure7.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure7.toString()) }
                if (!recette?.strMeasure8.isNullOrEmpty() && recette?.strMeasure8.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure8.toString()) }
                if (!recette?.strMeasure9.isNullOrEmpty() && recette?.strMeasure9.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure9.toString()) }
                if (!recette?.strMeasure10.isNullOrEmpty() && recette?.strMeasure10.toString().trim().isNotBlank()) { measures.add(recette?.strMeasure10.toString()) }

                mesuresList.addAll(measures.filter { it.trim().isNotEmpty() })
                adapterIngredient.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Recette>, t: Throwable) {
                Log.d("400", "Failure = " + t.toString());
            }


        })
    }
    private fun reloadActivity() {
        recreate()
    }

    private fun deleteRecette(id:String ){
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.deleteRecette(idRecette)
        call.enqueue(object:Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("200","success = "+response.body().toString());

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("400","Failure = "+t.toString());
            }
        })
    }



}