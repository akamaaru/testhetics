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

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var btnAuthenticate: Button
    private lateinit var tvRegistration: TextView
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText
    private lateinit var authentication: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    private fun init() {
        btnAuthenticate = findViewById(R.id.btn_authenticate)
        tvRegistration = findViewById(R.id.tv_registration)
        etLogin = findViewById(R.id.et_login)
        etPassword = findViewById(R.id.et_password)
        authentication = Firebase.auth
        progressBar = findViewById(R.id.pb_authentication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        setTitle(R.string.authentication_label)
        init()

        btnAuthenticate.setOnClickListener {
            val email = etLogin.text.toString()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Поля не могут быть пустыми!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            authentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            "Неверные логин или пароль!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

        tvRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
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