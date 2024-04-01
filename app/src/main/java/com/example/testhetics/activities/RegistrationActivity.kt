package com.example.testhetics.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testhetics.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var btnRegister: Button
    private lateinit var tvAuthentication: TextView
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText
    private lateinit var authentication: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    private fun init() {
        btnRegister = findViewById(R.id.btn_register)
        tvAuthentication = findViewById(R.id.tv_authentication)
        etLogin = findViewById(R.id.et_new_login)
        etPassword = findViewById(R.id.et_new_password)
        authentication = Firebase.auth
        progressBar = findViewById(R.id.pb_registration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        init()

        btnRegister.setOnClickListener {
            val email = etLogin.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Поля не могут быть пустыми!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            authentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this,
                            "Ошибка при регистрации",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        tvAuthentication.setOnClickListener {
            val intent = Intent(this, AuthenticationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = authentication.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}