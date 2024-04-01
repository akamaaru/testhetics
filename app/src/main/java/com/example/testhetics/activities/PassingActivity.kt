package com.example.testhetics.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.adapters.ChoiceAdapter
import com.example.testhetics.models.QuestionModel
import com.example.testhetics.utils.VariantsRecyclerViewInterface
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PassingActivity : DefaultActivity(), VariantsRecyclerViewInterface {
    lateinit var QUIZ_KEY: String
    lateinit var QUESTION_KEY: String
    lateinit var questions: ArrayList<QuestionModel>
    lateinit var progressBar: ProgressBar
    lateinit var databaseReference: DatabaseReference
    lateinit var practiceName: String
    lateinit var rvChoice: RecyclerView
    lateinit var choiceAdapter: ChoiceAdapter
    lateinit var btnNext: Button
    lateinit var tvTitle: TextView
    lateinit var tvQuestion: TextView
    lateinit var timer: CountDownTimer
    lateinit var timerHandler: Handler
    lateinit var tvTime: TextView
    var position = 0
    var variantsCount = 0
    var score = 0

    private fun init() {
        btnNext = findViewById(R.id.btn_next)
        questions = arrayListOf()
        rvChoice = findViewById(R.id.rv_choice)
        rvChoice.layoutManager = GridLayoutManager(this, 2)
        QUIZ_KEY = getString(R.string.QUIZ_KEY)
        QUESTION_KEY = getString(R.string.QUESTION_KEY)
        databaseReference = FirebaseDatabase.getInstance().getReference(QUIZ_KEY)
        practiceName = ""
        tvTitle = findViewById(R.id.tv_passing_question_title)
        tvQuestion = findViewById(R.id.tv_passing_question)
        progressBar = findViewById(R.id.progressBar)
        tvTime = findViewById(R.id.tv_time)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passing)

        init()

        val bundle = intent.extras
        if (bundle == null) {
            Toast.makeText(this, "Id квиза не найден...", Toast.LENGTH_SHORT).show()
            return
        }

        val practiceId = bundle.getInt("practice_id")
        databaseReference
            .child(practiceId.toString())
            .child("name")
            .get().addOnSuccessListener {
                practiceName = it.value.toString()
                setTitle(practiceName)
            }

        setTitle(practiceName)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(
                        this@PassingActivity,
                        "Такого вопроса нет!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                for (dataSnapshot in snapshot.children) {
                    val model = dataSnapshot.getValue(QuestionModel::class.java)
                    if (model != null) {
                        questions.add(model)
                    }
                }

                choiceAdapter = ChoiceAdapter(
                    this@PassingActivity,
                    this@PassingActivity,
                    questions[position].variants,
                    questions[position].correctAnswerIndex
                )

                rvChoice.adapter = choiceAdapter
                variantsCount = questions[position].variants.size

                val titleText = "Вопрос №${position + 1}"
                tvTitle.text = titleText
                tvQuestion.text = questions[position].question

                progressBar.max = 10
                progressBar.progress = progressBar.max

                val tvTimeText = "${progressBar.progress}c"
                tvTime.text = tvTimeText
                startTimer(progressBar.max)

                btnNext.setOnClickListener {
                    btnNextOnClickListener(it)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PassingActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        databaseReference
            .child(practiceId.toString())
            .child(QUESTION_KEY)
            .addValueEventListener(valueEventListener)
    }

    private fun btnNextOnClickListener(it: View) {
        btnNext.isEnabled = false
        btnNext.alpha = 0.5F

        if (++position >= questions.size) {
            val intent = Intent(it.context, ScoreActivity::class.java)
            intent.putExtra("score", score)
            intent.putExtra("questions_size", questions.size)
            startActivity(intent)
            finish()
            return
        }

        variantsCount = questions[position].variants.size
        choiceAdapter = ChoiceAdapter(
            this@PassingActivity,
            this@PassingActivity,
            questions[position].variants,
            questions[position].correctAnswerIndex
        )

        rvChoice.adapter = choiceAdapter
        val titleText = "Вопрос №${position + 1}"
        tvTitle.text = titleText
        tvQuestion.text = questions[position].question

        progressBar.max = 10
        progressBar.progress = progressBar.max

        val tvTimeText = "${progressBar.progress}c"
        tvTime.text = tvTimeText
        startTimer(progressBar.max)
    }

    fun startTimer(seconds: Int) {
        timer = object : CountDownTimer(
            (seconds * 1000).toLong(),
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                --progressBar.progress
                val tvTimeText = "${progressBar.progress}c"
                tvTime.text = tvTimeText
            }

            override fun onFinish() {
                timeUp()
            }
        }

        timerHandler = object : Handler(mainLooper) {}
        timerHandler.postDelayed({
            timer.start()
        }, 1000)
    }

    override fun checkAnswer(choice: ChoiceAdapter.ChoiceViewHolder) {
        val chosenIndex = rvChoice.getChildLayoutPosition(choice.itemView)

        for (i in 0..<rvChoice.childCount) {
            val holder: ChoiceAdapter.ChoiceViewHolder =
                rvChoice.findViewHolderForAdapterPosition(i) as ChoiceAdapter.ChoiceViewHolder
            holder.colorizeAnswered()
            holder.btnChoice.isEnabled = false
        }

        timerHandler.removeCallbacksAndMessages(null)
        timer.cancel()
        btnNext.isEnabled = true
        btnNext.alpha = 1F

        if (chosenIndex == questions[position].correctAnswerIndex) {
            ++score
        }
    }

    fun timeUp() {
        for (i in 0..<rvChoice.childCount) {
            val holder: ChoiceAdapter.ChoiceViewHolder =
                rvChoice.findViewHolderForAdapterPosition(i) as ChoiceAdapter.ChoiceViewHolder
            holder.colorize()
            holder.btnChoice.isEnabled = false
        }

        btnNext.isEnabled = true
        btnNext.alpha = 1F
    }
}