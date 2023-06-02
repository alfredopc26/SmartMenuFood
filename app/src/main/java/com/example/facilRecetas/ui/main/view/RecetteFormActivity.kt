package com.example.facilRecetas.ui.main.view



import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.facilRecetas.R
import com.example.facilRecetas.data.api.RestApiService
import com.example.facilRecetas.data.api.RetrofitInstance
import com.example.facilRecetas.data.models.Category
import com.example.facilRecetas.data.models.Ingredients
import com.example.facilRecetas.databinding.ActivityRecetteFormBinding
import com.example.facilRecetas.persistence.DatabaseFacilRecetas
import com.example.facilRecetas.persistence.RecetteEntity
import com.example.facilRecetas.utils.session.SessionPref
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class RecetteFormActivity : AppCompatActivity() {

    lateinit var imgView: ImageView
    lateinit var addButton: AppCompatButton
    lateinit var submitButton: Button
//    lateinit var imageUri: Uri
    lateinit var titreInput: EditText
    lateinit var descInput: EditText
    lateinit var areaInput: EditText
    lateinit var dureeInput: EditText
    lateinit var personInput: EditText
    lateinit var bioCheckBox: CheckBox
    lateinit var difficultyDropDown: AutoCompleteTextView
    lateinit var categoryDropDown: AutoCompleteTextView
    lateinit var listDifficulty: ArrayList<String>
    lateinit var path: String
    private val IMAGE_PICK_CODE = 1
    private val REQUEST_CODE = 420

    private lateinit var binding: ActivityRecetteFormBinding
    lateinit var ingredientsArray: ArrayList<String>
    lateinit var ingredientsTypeArray: ArrayList<String>

    lateinit var dialog: Dialog

    lateinit var editTextIngredient: EditText
    lateinit var listViewIngredient: ListView
    lateinit var addIngredient: AppCompatButton
    lateinit var removeIngredient: AppCompatButton

    lateinit var layoutArray: ArrayList<ConstraintLayout>
    lateinit var linearlayoutIngredients: LinearLayout
    lateinit var categoryList: ArrayList<String>

    //textView inputs
    lateinit var tv1: TextView
    lateinit var tv2: TextView
    lateinit var tv3: TextView
    lateinit var tv4: TextView
    lateinit var tv5: TextView
    lateinit var tv6: TextView
    lateinit var tv7: TextView
    lateinit var tv8: TextView
    lateinit var tv9: TextView
    lateinit var tv10: TextView
    lateinit var tv11: TextView
    lateinit var tv12: TextView
    lateinit var tv13: TextView
    lateinit var tv14: TextView
    lateinit var tv15: TextView
    lateinit var tv16: TextView
    lateinit var tv17: TextView
    lateinit var tv18: TextView
    lateinit var tv19: TextView
    lateinit var tv20: TextView

    //measure EditText
    lateinit var et1: EditText
    lateinit var et2: EditText
    lateinit var et3: EditText
    lateinit var et4: EditText
    lateinit var et5: EditText
    lateinit var et6: EditText
    lateinit var et7: EditText
    lateinit var et8: EditText
    lateinit var et9: EditText
    lateinit var et10: EditText
    lateinit var et11: EditText
    lateinit var et12: EditText
    lateinit var et13: EditText
    lateinit var et14: EditText
    lateinit var et15: EditText
    lateinit var et16: EditText
    lateinit var et17: EditText
    lateinit var et18: EditText
    lateinit var et19: EditText
    lateinit var et20: EditText

    //Type TextView
    lateinit var t1: TextView
    lateinit var t2: TextView
    lateinit var t3: TextView
    lateinit var t4: TextView
    lateinit var t5: TextView
    lateinit var t6: TextView
    lateinit var t7: TextView
    lateinit var t8: TextView
    lateinit var t9: TextView
    lateinit var t10: TextView
    lateinit var t11: TextView
    lateinit var t12: TextView
    lateinit var t13: TextView
    lateinit var t14: TextView
    lateinit var t15: TextView
    lateinit var t16: TextView
    lateinit var t17: TextView
    lateinit var t18: TextView
    lateinit var t19: TextView
    lateinit var t20: TextView

    lateinit var idUser: String
    lateinit var username: String
    lateinit var sessionPref: SessionPref
    lateinit var imageUrl: String

    lateinit var user: HashMap<String, String>
    var inc: Int = 0
    var checkIngredients: Boolean = false
    private lateinit var storageRef: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recette_form)
        initIngredients()
        ingredientsTypeArray = ArrayList()
        val measureType: List<String> = Arrays.asList("Mg", "Gr", "Kg", "Ml", "Li", "Un")
        ingredientsTypeArray.addAll(measureType)
        sessionPref = SessionPref(applicationContext)
        user = sessionPref.getUserPref()
        idUser = user.get(SessionPref.USER_ID).toString()
        username = user.get(SessionPref.USER_NAME).toString()
        storageRef = FirebaseStorage.getInstance().reference

        // Image Picker
        addButton = findViewById(R.id.add_image)
        imgView = findViewById(R.id.imageViewRecette)
        addButton.setOnClickListener {

            Log.d("addButton", "onClick: pickImageFromGallery")


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, REQUEST_CODE)
            } else {
                pickImageFromGallery()
            }
        }


        val toolbar = findViewById<Toolbar>(R.id.recetteFormBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        difficultyDropDown = findViewById(R.id.difficultyDropDown)

        listDifficulty = ArrayList()
        listDifficulty.add("Fácil")
        listDifficulty.add("Medio")
        listDifficulty.add("Difícil")

        val difficultyAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listDifficulty)

        difficultyDropDown.setAdapter(difficultyAdapter)
        difficultyDropDown.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG)
                .show()
        }
        setup()



        categoryDropDown = findViewById(R.id.catDropDown)
        loadCategoryList()

        val categoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoryList)

        categoryDropDown.setAdapter(categoryAdapter)
        categoryDropDown.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG)
                .show()
        }
        setup()


    }

    private fun pickImageFromGallery() {
        Log.d("addButton", "onClick: pickImageFromGallery")
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data!!


            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(imageUri))
            val fileName = "${FirebaseAuth.getInstance().currentUser?.uid}.${extension}"
            val fileRef = storageRef.child("images/$fileName")
            val uploadTask = fileRef.putFile(imageUri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageUrl = downloadUri.toString()
                } else {
                    val imageRef = storageRef.child("default_fondo.jpg")
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUrl = uri.toString()
                    }.addOnFailureListener { exception ->
                        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
                            imageUrl = data.data.toString()!!

                            // Utiliza la selectedImageUri según tus necesidades
                        }
                    }
                }
            }
        }
    }

    private fun loadCategoryList() {
        categoryList = ArrayList()
        val categories = DatabaseFacilRecetas.getInstance(applicationContext).categoryDao().getAllCategories()
        if (categories.size > 0) {
            for (cat in categories!!) {
                categoryList.add(cat.strCategory)
            }
        }
    }

    private fun setup() {
        //TextViews Ingredients
        tv1 = findViewById(R.id.IngredientTxt1)
        tv2 = findViewById(R.id.IngredientTxt2)
        tv3 = findViewById(R.id.IngredientTxt3)
        tv4 = findViewById(R.id.IngredientTxt4)
        tv5 = findViewById(R.id.IngredientTxt5)
        tv6 = findViewById(R.id.IngredientTxt6)
        tv7 = findViewById(R.id.IngredientTxt7)
        tv8 = findViewById(R.id.IngredientTxt8)
        tv9 = findViewById(R.id.IngredientTxt9)
        tv10 = findViewById(R.id.IngredientTxt10)
        tv11 = findViewById(R.id.IngredientTxt11)
        tv12 = findViewById(R.id.IngredientTxt12)
        tv13 = findViewById(R.id.IngredientTxt13)
        tv14 = findViewById(R.id.IngredientTxt14)
        tv15 = findViewById(R.id.IngredientTxt15)
        tv16 = findViewById(R.id.IngredientTxt16)
        tv17 = findViewById(R.id.IngredientTxt17)
        tv18 = findViewById(R.id.IngredientTxt18)
        tv19 = findViewById(R.id.IngredientTxt19)
        tv20 = findViewById(R.id.IngredientTxt20)
        //EditText Measures
        et1 = findViewById(R.id.MeasureTxt1)
        et2 = findViewById(R.id.MeasureTxt2)
        et3 = findViewById(R.id.MeasureTxt3)
        et4 = findViewById(R.id.MeasureTxt4)
        et5 = findViewById(R.id.MeasureTxt5)
        et6 = findViewById(R.id.MeasureTxt6)
        et7 = findViewById(R.id.MeasureTxt7)
        et8 = findViewById(R.id.MeasureTxt8)
        et9 = findViewById(R.id.MeasureTxt9)
        et10 = findViewById(R.id.MeasureTxt10)
        et11 = findViewById(R.id.MeasureTxt11)
        et12 = findViewById(R.id.MeasureTxt12)
        et13 = findViewById(R.id.MeasureTxt13)
        et14 = findViewById(R.id.MeasureTxt14)
        et15 = findViewById(R.id.MeasureTxt15)
        et16 = findViewById(R.id.MeasureTxt16)
        et17 = findViewById(R.id.MeasureTxt17)
        et18 = findViewById(R.id.MeasureTxt18)
        et19 = findViewById(R.id.MeasureTxt19)
        et20 = findViewById(R.id.MeasureTxt20)
        //TextView Measures
        t1 = findViewById(R.id.MeasureTxtType1)
        t2 = findViewById(R.id.MeasureTxtType2)
        t3 = findViewById(R.id.MeasureTxtType3)
        t4 = findViewById(R.id.MeasureTxtType4)
        t5 = findViewById(R.id.MeasureTxtType5)
        t6 = findViewById(R.id.MeasureTxtType6)
        t7 = findViewById(R.id.MeasureTxtType7)
        t8 = findViewById(R.id.MeasureTxtType8)
        t9 = findViewById(R.id.MeasureTxtType9)
        t10 = findViewById(R.id.MeasureTxtType10)
        t11 = findViewById(R.id.MeasureTxtType11)
        t12 = findViewById(R.id.MeasureTxtType12)
        t13 = findViewById(R.id.MeasureTxtType13)
        t14 = findViewById(R.id.MeasureTxtType14)
        t15 = findViewById(R.id.MeasureTxtType15)
        t16 = findViewById(R.id.MeasureTxtType16)
        t17 = findViewById(R.id.MeasureTxtType17)
        t18 = findViewById(R.id.MeasureTxtType18)
        t19 = findViewById(R.id.MeasureTxtType19)
        t20 = findViewById(R.id.MeasureTxtType20)

        imgView = findViewById(R.id.imageViewRecette)
        addButton = findViewById(R.id.add_image)
        submitButton = findViewById(R.id.submit_button)
        titreInput = findViewById(R.id.titreEditText)
        descInput = findViewById(R.id.descEditText)
        areaInput = findViewById(R.id.areaEditText)
        dureeInput = findViewById(R.id.dureeEditText)
        personInput = findViewById(R.id.personEditText)
        bioCheckBox = findViewById(R.id.bioCheckBox)
        difficultyDropDown = findViewById(R.id.difficultyDropDown)
        categoryDropDown = findViewById(R.id.catDropDown)
        addIngredient = findViewById(R.id.addIngredient)
        removeIngredient = findViewById(R.id.removeIngredient)
        linearlayoutIngredients = findViewById(R.id.ingredientInputs)

        removeIngredient.visibility = View.GONE


        for (i in 1 until linearlayoutIngredients.childCount - 1) {
            val view = linearlayoutIngredients.getChildAt(i)
            view.visibility = View.GONE
        }

        addIngredient.setOnClickListener {
            Log.d("+ INC", inc.toString())
            inc++
            linearlayoutIngredients.getChildAt(inc).visibility = View.VISIBLE

            if (inc == 1) {
                removeIngredient.visibility = View.VISIBLE
            }
            if (inc == 19) {
                addIngredient.visibility = View.GONE
            }
            Log.d("++ INC", inc.toString())
        }

        removeIngredient.setOnClickListener {
            Log.d("- INC", inc.toString())
            val ingredientInput = linearlayoutIngredients.getChildAt(inc) as ConstraintLayout
            val ingredientText = ingredientInput.getChildAt(0) as TextView
            val ingredientMeasureText = ingredientInput.getChildAt(2) as TextView
            val measureInput = ingredientInput.getChildAt(1) as TextInputLayout
            val measureEditText = measureInput.getChildAt(0) as FrameLayout
            val measureTxt = measureEditText.getChildAt(0) as EditText

            ingredientInput.visibility = View.GONE

            if (!ingredientText.text.isEmpty()) {
                ingredientsArray.add(ingredientText.text.toString())
                ingredientsArray.sorted()

                ingredientText.text = ""

            }
            ingredientMeasureText.text = ""
            measureTxt.text.clear()

            inc--
            if (inc == 0) {
                removeIngredient.visibility = View.GONE
            }
            if (inc == 18) {
                addIngredient.visibility = View.VISIBLE
            }
            Log.d("-- INC", inc.toString())

        }

        for (i in 0 until linearlayoutIngredients.childCount - 1) {
            val CL = linearlayoutIngredients.getChildAt(i) as ConstraintLayout
            val TV = CL.getChildAt(0) as TextView
            TV.setOnClickListener {
                showDialogSpinner(TV, ingredientsArray, 0)
            }

        }
        for (i in 0 until linearlayoutIngredients.childCount - 1) {
            val CL = linearlayoutIngredients.getChildAt(i) as ConstraintLayout
            val TV = CL.getChildAt(2) as TextView
            TV.setOnClickListener {
                showDialogSpinner(TV, ingredientsTypeArray, 1)
            }

        }

        addButton.setOnClickListener {
            pickImageFromGallery()


        }

        submitButton.setOnClickListener {
            Log.d("checkSubmit", "0")
            var bio = false
            if (bioCheckBox.isChecked) {
                bio = true
            }
            Log.d("checkSubmit", "1")

            for (i in 0 until inc) {
                val ingredientInput = linearlayoutIngredients.getChildAt(inc) as ConstraintLayout
                val ingredientText = ingredientInput.getChildAt(0) as TextView
                val measureInput = ingredientInput.getChildAt(1) as TextInputLayout
                val measureEditText = measureInput.getChildAt(0) as FrameLayout
                val measureTxt = measureEditText.getChildAt(0) as EditText
                val measureTypeTxt = ingredientInput.getChildAt(2) as TextView

                checkIngredients = validateIngredients(ingredientText, measureTxt, measureTypeTxt)
            }
            Log.d("checkSubmit", "2")
            Log.d("validate checkIngredients", checkIngredients.toString())
            if (validate(
                    titreInput,
                    descInput,
                    categoryDropDown,
                    areaInput,
                    dureeInput,
                    personInput,
                    difficultyDropDown
                ) && checkIngredients
            ) {
                Log.d("checkSubmit", "3")

                submit(
                    titreInput.text.toString(),
                    descInput.text.toString(),
                    categoryDropDown.text.toString(),
                    areaInput.text.toString(),
                    bio,
                    Integer.parseInt(dureeInput.text.toString()),
                    Integer.parseInt(personInput.text.toString()),
                    difficultyDropDown.text.toString(),
                    idUser,
                    username,
                    imageUrl,
                    tv1.text.toString(),
                    tv2.text.toString(),
                    tv3.text.toString(),
                    tv4.text.toString(),
                    tv5.text.toString(),
                    tv6.text.toString(),
                    tv7.text.toString(),
                    tv8.text.toString(),
                    tv9.text.toString(),
                    tv10.text.toString(),
                    tv11.text.toString(),
                    tv12.text.toString(),
                    tv13.text.toString(),
                    tv14.text.toString(),
                    tv15.text.toString(),
                    tv16.text.toString(),
                    tv17.text.toString(),
                    tv18.text.toString(),
                    tv19.text.toString(),
                    tv20.text.toString(),
                    measureFuse(et1, t1),
                    measureFuse(et2, t2),
                    measureFuse(et3, t3),
                    measureFuse(et4, t4),
                    measureFuse(et5, t5),
                    measureFuse(et6, t6),
                    measureFuse(et7, t7),
                    measureFuse(et8, t8),
                    measureFuse(et9, t9),
                    measureFuse(et10, t10),
                    measureFuse(et11, t11),
                    measureFuse(et12, t12),
                    measureFuse(et13, t13),
                    measureFuse(et14, t14),
                    measureFuse(et15, t15),
                    measureFuse(et16, t16),
                    measureFuse(et17, t17),
                    measureFuse(et18, t18),
                    measureFuse(et19, t19),
                    measureFuse(et20, t20)
                )

                Log.d("checkSubmit", "4")

//                retrofitUploadImage(imageUri)
            }


        }
//        val intent = Intent(this, FoodByIngredientsActivity::class.java)
//        startActivity(intent)
    }

    private fun measureFuse(et: EditText, t: TextView): String {
        val measure=""
        if (et.text.isEmpty() || t.text.isEmpty() ){
            return measure
        }else{
            return et.text.toString() + " " + t.text.toString()
        }

    }

    private fun showDialogSpinner(tv:TextView,list:ArrayList<String>,iM:Int) {
        val dialog = Dialog(this@RecetteFormActivity)
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window?.setLayout(1200, 1600)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        if (iM==0){
            dialog.findViewById<TextView>(R.id.spinnerHeader).text="Select an ingredient"
        }else if (iM==1){
            dialog.findViewById<TextView>(R.id.spinnerHeader).text="Select an Measure Type"
        }

        editTextIngredient = dialog.findViewById(R.id.searchIngredient)
        listViewIngredient = dialog.findViewById(R.id.listViewIngredients)
        

        val adapter = ArrayAdapter(
            this@RecetteFormActivity,
            android.R.layout.simple_list_item_1,
            list
        )
        listViewIngredient.adapter = adapter

        editTextIngredient.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        if (iM==0){
            listViewIngredient.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    if (tv.text.isEmpty()){
                        tv.text = (adapter.getItem(i) as String)
                        list.remove(adapter.getItem(i))
                        dialog.dismiss()
                    }else{
                        list.add(tv.text.toString())
                        tv.text = (adapter.getItem(i) as String)
                        list.remove(adapter.getItem(i))
                        dialog.dismiss()
                    }

                }
        }else if (iM==1){
            listViewIngredient.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, i, l ->
                    tv.text = (adapter.getItem(i) as String)
                    dialog.dismiss()
                }
        }


    }

    private fun checkDrop(a:String,b:ArrayList<String>):Boolean
    {
        var found = false
        for (n in b) {
            if (n == a) {
                found = true
                break
            }
        }
        return found

    }

    private fun validateIngredients(ingredientTxt: TextView,measureTxt:EditText,ingredientMeasureText:TextView ):Boolean{
        if(ingredientTxt.text.isEmpty() || measureTxt.text.isEmpty() || ingredientMeasureText.text.isEmpty()){
            if (ingredientTxt.text.isEmpty()){
                ingredientTxt.error="ingredient is required"
                ingredientTxt.requestFocus()
            }
            if (measureTxt.text.isEmpty()){
                measureTxt.error="measure is required"
                measureTxt.requestFocus()
            }
            if (ingredientMeasureText.text.isEmpty()){
                ingredientMeasureText.error="measure Type is required"
                ingredientMeasureText.requestFocus()
            }
         return false
        }
         return true

    }

    private fun validate(titreInput: EditText, descInput: EditText, category: AutoCompleteTextView,areaInput: EditText, dureeInput: EditText, personInput: EditText, difficulty: AutoCompleteTextView): Boolean {
        if (titreInput.text.isEmpty() || descInput.text.isEmpty() || dureeInput.text.isEmpty() || personInput.text.isEmpty() || areaInput.text.isEmpty() || !checkDrop(difficulty.text.toString(),listDifficulty) || !checkDrop(category.text.toString(),categoryList)) {

            if (titreInput.text.isEmpty()) {
                titreInput.error = "title is required"
                titreInput.requestFocus()
            }

            if (descInput.text.isEmpty()) {
                descInput.error = "description is required"
                descInput.requestFocus()

            }


            if (dureeInput.text.isEmpty()) {
                dureeInput.error = "duration does not match"
                dureeInput.requestFocus()

            }

            if (personInput.text.isEmpty()) {
                personInput.error = "person is required"
                personInput.requestFocus()

            }

            if (!checkDrop(difficultyDropDown.text.toString(),listDifficulty)) {
                difficulty.error = "difficulty is required"
                difficulty.requestFocus()

            }
            if (!checkDrop(categoryDropDown.text.toString(),categoryList)) {
                category.error = "Category is required"
                category.requestFocus()

            }

            return false
        }



        return true
    }
    
    private fun submit(
        name: String,
        description: String,
        category: String,
        area: String,
        isBio: Boolean,
        duration: Int,
        person: Int,
        difficulty: String,
        user: String,
        username:String,
        image: String,
        strIngredient1: String,strIngredient2: String,strIngredient3: String,strIngredient4: String,strIngredient5: String,strIngredient6: String,strIngredient7: String,strIngredient8: String,strIngredient9: String,strIngredient10: String,strIngredient11: String,strIngredient12: String,strIngredient13: String,strIngredient14: String,strIngredient15: String,strIngredient16: String,strIngredient17: String,strIngredient18: String,strIngredient19: String,strIngredient20: String,
        strMeasure1: String,strMeasure2: String,strMeasure3: String,strMeasure4: String,strMeasure5: String,strMeasure6: String,strMeasure7: String,strMeasure8: String,strMeasure9: String,strMeasure10: String,strMeasure11: String,strMeasure12: String,strMeasure13: String,strMeasure14: String,strMeasure15: String,strMeasure16: String,strMeasure17: String,strMeasure18: String,strMeasure19: String,strMeasure20: String

    )
    {

        val retIn =  DatabaseFacilRecetas.getInstance(applicationContext).recetteDao()
        val recetteCount = retIn.getAllRecette().size
        val recetteInfo = RecetteEntity(
            (recetteCount + 1).toString(),
            name,
            category,
            area,
            description,
            image,
            0,
            0,
            isBio,
            duration,
            person,
            difficulty,
            username,
            ArrayList<String>(),
            ArrayList<String>(),
            ArrayList<String>(),
            strIngredient1,
            strIngredient2,
            strIngredient3,
            strIngredient4,
            strIngredient5,
            strIngredient6,
            strIngredient7,
            strIngredient8,
            strIngredient9,
            strIngredient10,
            strMeasure1,
            strMeasure2,
            strMeasure3,
            strMeasure4,
            strMeasure5,
            strMeasure6,
            strMeasure7,
            strMeasure8,
            strMeasure9,
            strMeasure10
        )
        Log.d("check","4")
        retIn.insertRecette(recetteInfo)
        val intent = Intent(this@RecetteFormActivity, MainMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initIngredients() {
        ingredientsArray = ArrayList()
        val retIn = DatabaseFacilRecetas.getInstance(applicationContext).ingredientDao()
        val ingredients = retIn.getAllIngredient()
        if (ingredients.isNotEmpty()) {
            for (ingredient in ingredients!!) {
                ingredientsArray.add(ingredient.name)
            }
        }
    }
}