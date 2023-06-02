package com.example.facilRecetas.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.example.facilRecetas.R
import com.example.facilRecetas.data.models.User
import com.example.facilRecetas.databinding.ActivityRegisterBinding
import com.example.facilRecetas.utils.services.LoadingDialog
import com.example.facilRecetas.utils.session.SessionPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore



class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    lateinit var loginbtn : TextView
    lateinit var registerbtn : Button
    lateinit var sessionPref: SessionPref
    lateinit var loadingDialog: LoadingDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadingDialog = LoadingDialog(this)
        sessionPref = SessionPref(this)
        if (sessionPref.isLoggedIn()) {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        registerbtn = findViewById<Button>(R.id.RegisterBTN)

        registerbtn.setOnClickListener {
            val username = findViewById<EditText>(R.id.loginEditText)
            val password = findViewById<EditText>(R.id.passwordInputEditText)
            val verifPass = findViewById<EditText>(R.id.passwordInputEditText2)
            val email = findViewById<EditText>(R.id.emailEditText)
            val phone = findViewById<EditText>(R.id.phoneEditText)
            if (validateRegister(username, password, verifPass, email, phone)) {
                register(username.text.toString().trim(), password.text.toString().trim(), email.text.toString().trim(), phone.text.toString().trim())}
            }

        loginbtn = findViewById<TextView>(R.id.Logintext)

        loginbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateRegister(username: EditText, password: EditText, verifPass: EditText, email: EditText, phone: EditText): Boolean {
        if (username.text.trim().isEmpty() || password.text.trim().isEmpty() || verifPass.text.trim().isEmpty() || email.text.trim().isEmpty() || phone.text.trim().isEmpty()) {

            if (phone.text.isEmpty()) {
                phone.error = "Phone is required"
                phone.requestFocus()
            }

            if (email.text.isEmpty()) {
                email.error = "Email is required"
                email.requestFocus()

            }


            if (verifPass.text.isEmpty()) {
                verifPass.error = "Password does not match"
                verifPass.requestFocus()

            }

            if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()

            }

            if (username.text.isEmpty()) {
                username.error = "Username is required"
                username.requestFocus()

            }

            return false
        }

        //Patterns // Regex // Length
        if (password.text.length < 6){
            password.error = "Password must be at least 6 characters"
            password.requestFocus()
            return false
        }

        if (password.text.toString() != verifPass.text.toString()){
            verifPass.error = "Password does not match"
            verifPass.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            email.error = "Email unvalid"
            email.requestFocus()
            return false
        }

        if(phone.text.length != 10){
            phone.error = "Phone number must be 8 digits"
            phone.requestFocus()
            return false
        }

        if(!phone.text.toString().trim().matches(Regex("[0-9]+"))){
            phone.error = "Phone number must be digits"
            phone.requestFocus()
            return false
        }

        return true
    }

    private fun register(username: String, password: String, email: String, phone: String)  {
        loadingDialog.startLoadingDialog()


        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = mAuth.currentUser
                val userId = user?.uid
                if (userId != null) {
                    val db = FirebaseFirestore.getInstance()
                    val usuario = User(userId, username, email, password, phone)
                    db.collection("usuarios")
                        .add(usuario)
                        .addOnSuccessListener {  documentReference ->
                            loadingDialog.dismissDialog()
                            sessionPref.createRegisterSession(userId, username, email, password,phone)
                            Toast.makeText(this@RegisterActivity, "Welcome!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainMenuActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            loadingDialog.dismissDialog()
                            Toast.makeText(this@RegisterActivity, "Error al guardar los datos: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }

            } else {
                loadingDialog.dismissDialog()
                // Error en el registro, mostrar mensaje al usuario
                Toast.makeText(this@RegisterActivity, "Error en el registro: " + task.exception?.message,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}