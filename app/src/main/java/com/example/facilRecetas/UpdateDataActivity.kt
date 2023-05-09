package com.example.facilRecetas


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.persistence.CategoryEntity
import com.example.facilRecetas.data.models.Category
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.ui.main.view.MainMenuActivity
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.persistence.RecetteEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDataActivity: AppCompatActivity() {
    private lateinit var apiService: RestApiService
    private lateinit var dCategories: CategoryEntity
    private lateinit var dRecette: RecetteEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)
        apiService = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

        UpdateDataTask().execute()
    }

    inner class UpdateDataTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val call_categories = apiService.getCategoriesList()
            call_categories.enqueue(object : Callback<List<Category>> {
                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    if (response.isSuccessful) {
                        val _categories = response.body()
                        if (_categories != null) {
                            if(_categories.size > 0){
                                DatabaseFacilRecetas.getInstance(applicationContext).categoryDao().deleteAllCategory()
                                for (category in _categories!!) {
                                    dCategories = CategoryEntity(category.id, category.name, category.image, category.description)
                                    DatabaseFacilRecetas.getInstance(applicationContext).categoryDao().insertCategory(dCategories)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    Log.d("400", "Failure = " + t.toString());
                }
            })

            val call_recettes = apiService.getRecette()
            call_recettes.enqueue(object : Callback<List<Recette>> {
                override fun onResponse(
                    call: Call<List<Recette>>,
                    response: Response<List<Recette>>
                ) {
                    if (response.isSuccessful) {
                        val _recettes = response.body()
                        if (_recettes != null) {
                            DatabaseFacilRecetas.getInstance(applicationContext).recetteDao().deleteAllRecette()
                            if(_recettes.size > 0){
                                for (recette in _recettes!!) {
                                    dRecette = RecetteEntity(
                                        recette.id,
                                        recette.name,
                                        recette.description,
                                        recette.image,
                                        recette.likes,
                                        recette.dislikes,
                                        recette.isBio,
                                        recette.duration,
                                        recette.person,
                                        recette.difficulty,
                                        recette.username,
                                        recette.comments,
                                        recette.usersLiked,
                                        recette.usersDisliked,
                                        recette.strIngredient1,
                                        recette.strIngredient2,
                                        recette.strIngredient3,
                                        recette.strIngredient4,
                                        recette.strIngredient5,
                                        recette.strIngredient6,
                                        recette.strIngredient7,
                                        recette.strIngredient8,
                                        recette.strIngredient9,
                                        recette.strIngredient10,
                                        recette.strMeasure1,
                                        recette.strMeasure2,
                                        recette.strMeasure3,
                                        recette.strMeasure4,
                                        recette.strMeasure5,
                                        recette.strMeasure6,
                                        recette.strMeasure7,
                                        recette.strMeasure8,
                                        recette.strMeasure9,
                                        recette.strMeasure10
                                    )
                                    DatabaseFacilRecetas.getInstance(applicationContext).recetteDao().insertRecette(dRecette)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Recette>>, t: Throwable) {
                    Log.d("400", "Failure = " + t.toString());
                }
            })

            return null
        }

        override fun onPostExecute(result: Void?) {
            // Actualizar la vista o navegar a otra actividad
            // Aquí puedes escribir el código para navegar a la actividad que necesites después de que los datos han sido actualizados
            val intent = Intent(applicationContext, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}