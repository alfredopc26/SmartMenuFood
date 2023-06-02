package com.example.facilRecetas.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facilRecetas.R
import com.example.facilRecetas.utils.services.LoadingDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class PasswordForgetActivity: AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    lateinit var passwordLayout: TextInputLayout
    lateinit var loadingDialog: LoadingDialog

    lateinit var emailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)
        loadingDialog = LoadingDialog(this)
        FirebaseApp.initializeApp(applicationContext)

        emailEditText = findViewById(R.id.emailEditText)

        val loginBtn = findViewById<TextView>(R.id.LoginTV)
        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val recoveryBtn = findViewById<Button>(R.id.RecoveryBtn)
        recoveryBtn.setOnClickListener() {
            if (validateRecovery(emailEditText)) {
                changePassword(emailEditText.text.trim().toString())

            }else{
                val toast = Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT)
                toast.show()
            }

        }

    }

    private fun validateRecovery(emailEditText: EditText): Boolean {
        if(emailEditText.text.trim().isEmpty()){
            // made it revesed so it desplays correctly you ll see it in the app
            if (emailEditText.text.isEmpty()) {
                emailEditText.error = "Email es requerido"
                emailEditText.requestFocus()

            }

            return false

        }

        return true
    }

    private fun changePassword(email: String) {
        loadingDialog.startLoadingDialog()
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    loadingDialog.dismissDialog()
                    Toast.makeText(this, "Se ha enviado un correo electrónico para restablecer tu contraseña", Toast.LENGTH_SHORT).show()
                }
                else {
                    loadingDialog.dismissDialog()
                    Toast.makeText(this@PasswordForgetActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}