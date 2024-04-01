package com.example.testhetics.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.models.QuizModel
import com.example.testhetics.R
import com.example.testhetics.adapters.QuestionAdapter
import com.example.testhetics.models.QuestionModel
import com.example.testhetics.utils.MarginItemDecoration
import com.example.testhetics.utils.QuestionsRecyclerViewInterface
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateQuizActivity : DefaultActivity(), QuestionsRecyclerViewInterface {
    lateinit var QUIZ_KEY: String
    lateinit var databaseReference: DatabaseReference
    lateinit var progressDialog: ProgressDialog
    lateinit var btnAddQuestion: Button
    lateinit var btnCreate: Button
    lateinit var etName: EditText
    lateinit var etDescription: EditText
    lateinit var questions: ArrayList<QuestionModel>
    lateinit var adapter: QuestionAdapter
    lateinit var recyclerView: RecyclerView

    private fun init() {
        QUIZ_KEY = getString(R.string.QUIZ_KEY)

        etName = findViewById(R.id.et_name)
        etDescription = findViewById(R.id.et_description)
        btnAddQuestion = findViewById(R.id.btn_add_question)
        btnCreate = findViewById(R.id.btn_create)
        databaseReference = FirebaseDatabase.getInstance().getReference(QUIZ_KEY)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Создание нового квиза")
        progressDialog.setMessage("Пожалуйста, подождите")

        questions = arrayListOf()
        adapter = QuestionAdapter(
            this,
            questions
        )

        recyclerView = findViewById(R.id.rv_questions)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(MarginItemDecoration(16))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_quiz)

        init()

        btnAddQuestion.setOnClickListener {
            questions.add(QuestionModel())
            adapter.notifyItemInserted(questions.size - 1)
        }

        btnCreate.setOnClickListener {
            btnCreateOnClickListener()
        }
    }

    override fun onItemDelete(position: Int) {
        questions.removeAt(position)
        adapter.notifyItemRemoved(position)

        for (i in position..<questions.size) {
            adapter.notifyItemChanged(i)
        }
    }

    private fun btnCreateOnClickListener() {
        val name = etName.text.toString()
        val description = etDescription.text.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Квиз должен иметь название!", Toast.LENGTH_SHORT).show()
            return
        }

        progressDialog.show()

        val newQuizModel = QuizModel(name, description, questions)
        databaseReference.child(newQuizModel.id.toString()).setValue(newQuizModel)

        progressDialog.dismiss()

        Toast.makeText(this, "Квиз успешно создан!", Toast.LENGTH_SHORT).show()
        finish()
    }
}