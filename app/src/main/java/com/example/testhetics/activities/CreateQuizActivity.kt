package com.example.testhetics.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.adapters.QuestionAdapter
import com.example.testhetics.models.QuestionModel
import com.example.testhetics.models.QuizModel
import com.example.testhetics.utils.MarginItemDecoration
import com.example.testhetics.utils.QuestionsRecyclerViewInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateQuizActivity : DefaultActivity(), QuestionsRecyclerViewInterface {
    private val requestCodeSelectImage = 100
    private lateinit var QUIZ_KEY: String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnAddQuestion: Button
    private lateinit var btnCreate: Button
    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var questions: ArrayList<QuestionModel>
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var recyclerView: RecyclerView

    private fun init() {
        QUIZ_KEY = getString(R.string.QUIZ_KEY)

        etName = findViewById(R.id.et_name)
        etDescription = findViewById(R.id.et_description)
        btnAddQuestion = findViewById(R.id.btn_add_question)
        btnCreate = findViewById(R.id.btn_create)
        databaseReference = FirebaseDatabase.getInstance().getReference(QUIZ_KEY)
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Создание нового квиза")
        progressDialog.setMessage("Пожалуйста, подождите")

        questions = arrayListOf(QuestionModel())
        questionAdapter = QuestionAdapter(
            this,
            questions
        )

        recyclerView = findViewById(R.id.rv_questions)
        recyclerView.adapter = questionAdapter
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
            questionAdapter.notifyItemInserted(questions.size - 1)
        }

        btnCreate.setOnClickListener {
            btnCreateOnClickListener()
        }
    }

    override fun onQuestionCheck(questionPosition: Int, variantPosition: Int) {
        questions[questionPosition].correctAnswerIndex = variantPosition
    }

    override fun onQuestionDelete(questionPosition: Int) {
        questions.removeAt(questionPosition)
        questionAdapter.notifyItemRemoved(questionPosition)

        for (i in questionPosition..<questions.size) {
            questionAdapter.notifyItemChanged(i)
        }
    }

    override fun onSelectImage() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        ActivityCompat.startActivityForResult(
            this,
            galleryIntent,
            requestCodeSelectImage,
            null
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestCodeSelectImage
            && resultCode == RESULT_OK
            && data != null
            && data.data != null) {
            val position = questionAdapter.currentImagePosition
            (recyclerView.findViewHolderForAdapterPosition(position) as QuestionAdapter.QuestionViewHolder)
                .upload(data.data!!)
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

        val newQuizModel = QuizModel(
            name,
            description,
            questions,
            auth.currentUser!!.email!!
        )
        databaseReference.child(newQuizModel.id.toString()).setValue(newQuizModel)

        progressDialog.dismiss()

        Toast.makeText(this, "Квиз успешно создан!", Toast.LENGTH_SHORT).show()
        finish()
    }
}