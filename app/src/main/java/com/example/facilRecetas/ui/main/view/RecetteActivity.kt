package com.example.facilRecetas.ui.main.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Comment
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.ui.main.adapter.CommentAdapter
import com.example.facilRecetas.ui.main.adapter.IngredientsRecetteTextAdapter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecetteActivity : AppCompatActivity() {

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
    private lateinit var likeButton:AppCompatImageButton
    private lateinit var dislikeButton:AppCompatImageButton
    lateinit var defaultArea: String
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
    lateinit var toolbar: Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recette)

        idRecette = intent.getStringExtra("id").toString()
        Log.d("IDRECETTE",idRecette)

        ingredientsList = ArrayList()
        mesuresList = ArrayList()



        val layoutManagerIngredient = LinearLayoutManager(this)

        recyclerViewIngredient = findViewById(R.id.ingredientListRecetteView)

        recyclerViewIngredient.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewIngredient.setHasFixedSize(true)





        val layoutManager = LinearLayoutManager(this)

        recyclerView = findViewById(R.id.commentSession)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        commentInput = findViewById(R.id.commentEditText)
        recetteTitre = findViewById(R.id.foodRecipeName)
        timeTxt = findViewById(R.id.timeTxt)
        peopleTxt = findViewById(R.id.peopleTxt)
        difficultyTxt = findViewById(R.id.difficultyTxt)
        instructionTxt = findViewById(R.id.foodRecipeInstructions)
        recetteUser = findViewById(R.id.foodRecipeCategory)
        likeButton = findViewById(R.id.upvoteButton)
        dislikeButton = findViewById(R.id.downvoteButton)
        recetteLikes=findViewById(R.id.recetteLikes)
        submitComment=findViewById(R.id.submitComment)

        submitComment.setOnClickListener {
        if (commentInput.text.isEmpty()){
            commentInput.error = "comment is required"
            commentInput.requestFocus()
        }else{
            postComment(commentInput.text.toString())
            commentInput.text.clear()
            reloadActivity()
        }

        }
        toolbar = findViewById(R.id.recetteRecipeToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Receta"
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        initRecette()
    }
    private fun initRecette() {
        val ingredients = ArrayList<String>()
        val measures = ArrayList<String>()
        
        val retIn = DatabaseFacilRecetas.getInstance(applicationContext).recetteDao()
        val recette = retIn.getRecetteById(idRecette)

        if(recette._id != "") {
            recetteTitre.text = recette!!.name
            timeTxt.text = recette!!.duration.toString()
            peopleTxt.text = recette!!.person.toString()
            difficultyTxt.text = recette!!.difficulty
            instructionTxt.text = recette!!.description
            recetteUser.text = recette!!.username
            recetteLikes.text =
                (recette!!.likes.toFloat() - recette!!.dislikes.toFloat()).toInt()
                    .toString()

            if (!recette?.strIngredient1.isNullOrEmpty() && recette?.strIngredient1.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient1.toString())
            }
            if (!recette?.strIngredient2.isNullOrEmpty() && recette?.strIngredient2.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient2.toString())
            }
            if (!recette?.strIngredient3.isNullOrEmpty() && recette?.strIngredient3.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient3.toString())
            }
            if (!recette?.strIngredient4.isNullOrEmpty() && recette?.strIngredient4.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient4.toString())
            }
            if (!recette?.strIngredient5.isNullOrEmpty() && recette?.strIngredient5.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient5.toString())
            }
            if (!recette?.strIngredient6.isNullOrEmpty() && recette?.strIngredient6.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient6.toString())
            }
            if (!recette?.strIngredient7.isNullOrEmpty() && recette?.strIngredient7.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient7.toString())
            }
            if (!recette?.strIngredient8.isNullOrEmpty() && recette?.strIngredient8.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient8.toString())
            }
            if (!recette?.strIngredient9.isNullOrEmpty() && recette?.strIngredient9.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient9.toString())
            }
            if (!recette?.strIngredient10.isNullOrEmpty() && recette?.strIngredient10.toString()
                    .trim().isNotBlank()
            ) {
                ingredients.add(recette?.strIngredient10.toString())
            }

            ingredientsList.addAll(ingredients.filter { it.trim().isNotEmpty() })

            if (!recette?.strMeasure1.isNullOrEmpty() && recette?.strMeasure1.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure1.toString())
            }
            if (!recette?.strMeasure2.isNullOrEmpty() && recette?.strMeasure2.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure2.toString())
            }
            if (!recette?.strMeasure3.isNullOrEmpty() && recette?.strMeasure3.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure3.toString())
            }
            if (!recette?.strMeasure4.isNullOrEmpty() && recette?.strMeasure4.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure4.toString())
            }
            if (!recette?.strMeasure5.isNullOrEmpty() && recette?.strMeasure5.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure5.toString())
            }
            if (!recette?.strMeasure6.isNullOrEmpty() && recette?.strMeasure6.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure6.toString())
            }
            if (!recette?.strMeasure7.isNullOrEmpty() && recette?.strMeasure7.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure7.toString())
            }
            if (!recette?.strMeasure8.isNullOrEmpty() && recette?.strMeasure8.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure8.toString())
            }
            if (!recette?.strMeasure9.isNullOrEmpty() && recette?.strMeasure9.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure9.toString())
            }
            if (!recette?.strMeasure10.isNullOrEmpty() && recette?.strMeasure10.toString().trim()
                    .isNotBlank()
            ) {
                measures.add(recette?.strMeasure10.toString())
            }

            mesuresList.addAll(measures.filter { it.trim().isNotEmpty() })
        }
    }
    private fun reloadActivity() {
        recreate()
    }


    private fun postComment(commentText:String) {

        val commentInfo = Comment(
            commentText,
            idRecette,
            idUser,
            username
        )
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = retIn.postComment(commentInfo)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(this@RecetteActivity, "comment Added", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(this@RecetteActivity, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
                }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("400","Failure = "+t.toString());
            }

        })
    }

}