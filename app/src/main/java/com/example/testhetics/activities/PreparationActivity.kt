package com.example.testhetics.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.testhetics.R

class PreparationActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preparation)

        val btnBegin = findViewById<Button>(R.id.btn_begin)
        btnBegin.setOnClickListener {
            val intent = Intent(this, PassingActivity::class.java)

            val bundle = getIntent().extras
            if (bundle == null) {
                Toast.makeText(this, "Id квиза не найден...", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val practiceId = bundle.getInt("practice_id")
            intent.putExtra("practice_id", practiceId)

            startActivity(intent)
        }
    }
}