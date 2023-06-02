package com.example.facilRecetas.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.example.facilRecetas.R
import com.example.facilRecetas.data.models.User
import com.example.facilRecetas.utils.services.LoadingDialog
import com.example.facilRecetas.utils.session.SessionPref
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    private val RC_SIGN_IN = 123
    private lateinit var googleSignInButton: SignInButton
    lateinit var sessionPref: SessionPref

    lateinit var usernameLayout: TextInputLayout
    lateinit var passwordLayout: TextInputLayout
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loadingDialog: LoadingDialog
    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingDialog = LoadingDialog(this)
        FirebaseApp.initializeApp(applicationContext)
        oneTapClient = Identity.getSignInClient(this)

        //our login session manager
        sessionPref = SessionPref(this)
        if (sessionPref.isLoggedIn()) {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        usernameLayout = findViewById(R.id.LoginInputLayout)
        passwordLayout = findViewById(R.id.PasswordInputLayout)
        usernameEditText = findViewById(R.id.loginEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        val registerBtn = findViewById<TextView>(R.id.RegisterTV)


        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val forgetBtn = findViewById<TextView>(R.id.forget_pass)

        forgetBtn.setOnClickListener {
            val intent = Intent(this, PasswordForgetActivity::class.java)
            startActivity(intent)
        }

        googleSignInButton = findViewById(R.id.googleSignInButton)
        mAuth = FirebaseAuth.getInstance()

        val loginBtn = findViewById<Button>(R.id.LoginBtn)
        loginBtn.setOnClickListener() {
            if (validateLogin(usernameEditText, passwordEditText,passwordLayout)) {
                login(usernameEditText.text.trim().toString(), passwordEditText.text.trim().toString())

            }else{
                val toast = Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT)
                toast.show()
            }

        }


        googleSignInButton.setOnClickListener {
            startSignIn()
        }
    }

    private fun startSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build() // Proveedor de inicio de sesión con Google
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Inicio de sesión exitoso, obtener el usuario actual
                val user = mAuth.currentUser.toString()
                user?.let {
                    loadingDialog.dismissDialog()
                    val gson = Gson()
                    val jsonSTRING = user
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    val user = jsonObject.get("user").asJsonObject
                    val id_user = user.get("_id").asString
                    val username_user = user.get("username").asString
                    val email_user = user.get("email").asString
                    val phone_user = user.get("phone").asString
                    sessionPref.createRegisterSession(id_user, username_user, email_user, "",phone_user)
                    Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                // Inicio de sesión fallido
                response?.error?.message?.let { Log.e("LoginActivity", it) }
            }
        }
    }

    private fun validateLogin(username: EditText, password: EditText,passwordlayout : TextInputLayout): Boolean {
        if(username.text.trim().isEmpty() || password.text.trim().isEmpty()){

            if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()

            }
            // made it revesed so it desplays correctly you ll see it in the app
            if (username.text.isEmpty()) {
                username.error = "Username is required"
                username.requestFocus()

            }

            return false

        }

        return true
    }

    private fun login(email: String, password: String) {
        loadingDialog.startLoadingDialog()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    loadingDialog.dismissDialog()
                    val gson = Gson()
                    val jsonSTRING = mAuth.currentUser.toString()
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    val user = jsonObject.get("user").asJsonObject
                    val id_user = user.get("_id").asString
                    val username_user = user.get("username").asString
                    val email_user = user.get("email").asString
                    val phone_user = user.get("phone").asString
                    sessionPref.createRegisterSession(id_user, username_user, email_user, "",phone_user)
                    Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    loadingDialog.dismissDialog()
                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

