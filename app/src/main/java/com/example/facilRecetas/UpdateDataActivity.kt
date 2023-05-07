package com.example.facilRecetas


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.persistence.CategoryEntity
import com.example.facilRecetas.data.models.Category
import com.example.facilRecetas.ui.main.view.MainMenuActivity
import com.example.facilRecetas.persistence.CategoryDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDataActivity: AppCompatActivity() {
    private lateinit var apiService: RestApiService
    private lateinit var dCategories: CategoryEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)
        apiService = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

        UpdateDataTask().execute()
    }

    inner class UpdateDataTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void?): Void? {
            val call = apiService.getCategoriesList()
            call.enqueue(object : Callback<List<Category>> {
                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    if (response.isSuccessful) {
                        val _categories = response.body()
                        if (_categories != null) {
                            if(_categories.size > 0){
                                CategoryDatabase.getInstance(applicationContext).categoryDao().deleteAllCategory()
                                for (category in _categories!!) {
                                    dCategories = CategoryEntity(category.id, category.name, category.image, category.description)
                                    CategoryDatabase.getInstance(applicationContext).categoryDao().insertCategory(dCategories)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
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