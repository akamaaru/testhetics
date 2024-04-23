package com.example.testhetics.activities

import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.testhetics.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PreparationActivity : DefaultActivity() {
    private lateinit var QUIZ_KEY: String
    private lateinit var QUESTION_KEY: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var btnBegin: Button
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvType: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvQuestions: TextView
    private var bundle: Bundle? = null
    private var practiceId = 0

    private fun init() {
        practiceId = bundle!!.getInt("practice_id")
        QUIZ_KEY = getString(R.string.QUIZ_KEY)
        QUESTION_KEY = getString(R.string.QUESTION_KEY)
        databaseReference = FirebaseDatabase.getInstance()
            .getReference(QUIZ_KEY)
            .child(practiceId.toString())
        btnBegin = findViewById(R.id.btn_begin)
        tvName = findViewById(R.id.tv_prep_name)
        tvDescription = findViewById(R.id.tv_prep_description)
        tvType = findViewById(R.id.tv_prep_type)
        tvAuthor = findViewById(R.id.tv_prep_author)
        tvQuestions = findViewById(R.id.tv_prep_questions)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preparation)

        bundle = intent.extras
        if (bundle == null) {
            Toast.makeText(this, "Id квиза не найден...", Toast.LENGTH_SHORT).show()
            return
        }

        init()
        setTextViews()

        btnBegin.setOnClickListener {
            val intent = Intent(this, PassingActivity::class.java)
            intent.putExtra("practice_id", practiceId)
            startActivity(intent)
        }
    }

    private fun setTextViews() {
        tvType.text = "Квиз"

        databaseReference
            .child("name")
            .get().addOnSuccessListener {
                tvName.text = it.value.toString()
            }

        databaseReference
            .child("description")
            .get().addOnSuccessListener {
                tvDescription.text = it.value.toString()
                if (tvDescription.text.isEmpty()) {
                    tvDescription.visibility = View.GONE
                }
            }

        databaseReference
            .child("author")
            .get().addOnSuccessListener {
                tvAuthor.text = it.value.toString()
            }

        databaseReference
            .child(QUESTION_KEY)
            .get().addOnSuccessListener {
                tvQuestions.text = it.childrenCount.toString()
            }
    }
}