package com.example.facilRecetas


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Category
import com.example.facilRecetas.data.models.Recette
import com.example.facilRecetas.persistence.CategoryEntity
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.persistence.IngredientEntity
import com.example.facilRecetas.persistence.RecetteEntity
import com.example.facilRecetas.ui.main.view.MainMenuActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UpdateDataActivity: AppCompatActivity() {
    private lateinit var apiService: RestApiService
    private lateinit var dCategories: CategoryEntity
    private lateinit var dRecette: RecetteEntity
    private lateinit var dIngredients: IngredientEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)
        apiService = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

        UpdateDataTask().execute()
    }

    inner class UpdateDataTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            getInfoCategories()
            getInfoRecettes()
            getInfoIngredients()
            return null
        }

        override fun onPostExecute(result: Void?) {
            // Actualizar la vista o navegar a otra actividad
            // Aquí puedes escribir el código para navegar a la actividad que necesites después de que los datos han sido actualizados
            val intent = Intent(applicationContext, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        fun readJsonFromAssets(context: Context, fileName: String): String {
            return context.assets.open(fileName).bufferedReader().use { it.readText() }
        }

        fun getMapArrayList(fileName: String): List<Map<String, Any>>{
            val fileJson = readJsonFromAssets(applicationContext, fileName)
            val listType = object : TypeToken<List<Map<String, Any>>>() {}.type
            val gson = Gson()
            val lisMap = gson.fromJson<List<Map<String, Any>>>(fileJson, listType)

            return lisMap
        }
        fun getInfoCategories() {
            val categoryList = getMapArrayList("json/categories.json")
            //val callCategories = apiService.getCategoriesList()
            if(categoryList.isNotEmpty()){
                DatabaseFacilRecetas.getInstance(applicationContext).categoryDao().deleteAllCategory()
                for (category in categoryList!!) {
                    dCategories = CategoryEntity(
                        category["_id"] as String,
                        category["strCategory"] as String,
                        category["strCategoryThumb"] as String,
                        category["strCategoryDescription"] as String
                    )
                    DatabaseFacilRecetas.getInstance(applicationContext).categoryDao().insertCategory(dCategories)
                }
            }
        }

        fun getInfoRecettes() {
            val recetteList = getMapArrayList("json/recette.json")
            if(recetteList.isNotEmpty()){
                for (recette in recetteList!!) {
                    val likesDouble = recette["likes"] as? Double
                    val dislikesDouble = recette["dislikes"] as? Double
                    val durationDouble = recette["duration"] as? Double
                    val personDouble = recette["person"] as? Double
                    dRecette = RecetteEntity(
                            recette["_id"] as String,
                            recette["name"] as String,
                            recette["category"] as String,
                            recette["area"] as String,
                            recette["description"] as String,
                            recette["image"] as String,
                        likesDouble?.toInt() ?: 0 ,
                      dislikesDouble?.toInt() ?: 0 ,
                            recette["isBio"] as Boolean,
                        durationDouble?.toInt() ?: 0 ,
                        personDouble?.toInt() ?: 0 ,
                            recette["difficulty"] as String,
                            recette["username"] as String,
                            recette["comments"] as ArrayList<String>,
                            recette["usersLiked"] as ArrayList<String>,
                            recette["usersDisliked"] as ArrayList<String>,
                            recette["strIngredient1"] as String,
                            recette["strIngredient2"] as String,
                            recette["strIngredient3"] as String,
                            recette["strIngredient4"] as String,
                            recette["strIngredient5"] as String,
                            recette["strIngredient6"] as String,
                            recette["strIngredient7"] as String,
                            recette["strIngredient8"] as String,
                            recette["strIngredient9"] as String,
                            recette["strIngredient10"] as String,
                            recette["strMeasure1"] as String,
                            recette["strMeasure2"] as String,
                            recette["strMeasure3"] as String,
                            recette["strMeasure4"] as String,
                            recette["strMeasure5"] as String,
                            recette["strMeasure6"] as String,
                            recette["strMeasure7"] as String,
                            recette["strMeasure8"] as String,
                            recette["strMeasure9"] as String,
                            recette["strMeasure1"] as String
                    )
                    DatabaseFacilRecetas.getInstance(applicationContext).recetteDao().insertRecette(dRecette)
                }
            }
        }
        fun getInfoIngredients() {
            val ingredientsList = getMapArrayList("json/ingredients.json")
            if(ingredientsList.isNotEmpty()){
                DatabaseFacilRecetas.getInstance(applicationContext).ingredientDao().deleteAllIngredient()
                for (ingredient in ingredientsList!!) {
                    dIngredients = IngredientEntity(
                        ingredient["_id"] as String,
                        ingredient["strIngredient"] as String,
                        ingredient["strDescription"] as String,
                    )
                    DatabaseFacilRecetas.getInstance(applicationContext).ingredientDao().insertIngredient(dIngredients)
                }
                }
            }

    }
}