package com.example.testhetics.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.testhetics.R
import com.google.android.material.progressindicator.CircularProgressIndicator

class ScoreActivity : DefaultActivity() {
    private lateinit var imgTrophy: ImageView
    private lateinit var tvCongrats: TextView
    private lateinit var indicator: CircularProgressIndicator
    private lateinit var tvScorePercent: TextView
    private lateinit var tvCorrect: TextView
    private lateinit var tvIncorrect: TextView
    private lateinit var tvTime: TextView

    private fun init() {
        imgTrophy = findViewById(R.id.img_trophy)
        tvCongrats = findViewById(R.id.tv_congrats)
        indicator = findViewById(R.id.score_indicator)
        tvScorePercent = findViewById(R.id.tv_score_percent)
        tvCorrect = findViewById(R.id.tv_correct)
        tvIncorrect = findViewById(R.id.tv_incorrect)
        tvTime = findViewById(R.id.tv_time_value)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        init()

        val bundle = intent.extras
        if (bundle == null) {
            Toast.makeText(this, "Данные о прохождении не найдены...", Toast.LENGTH_SHORT).show()
            return
        }

        val score = bundle.getInt("score")
        val questionsCount = bundle.getInt("questions_size")
        val time = bundle.getInt("time")

        tvCorrect.text = score.toString()
        tvIncorrect.text = (questionsCount - score).toString()
        val tvTimeText = "${time}c"
        tvTime.text = tvTimeText

        indicator.max = questionsCount
        indicator.progress = score

        var ratio = 0.0
        if (questionsCount != 0) {
            ratio = score.toDouble() / questionsCount
        }

        if (ratio >= 2.0 / 3) {
            imgTrophy.setBackgroundResource(R.drawable.trophy)
            tvCongrats.text = "Поздравляем!"
        } else if (ratio >= 1.0 / 3) {
            imgTrophy.setBackgroundResource(R.drawable.thumb)
            tvCongrats.text = "Неплохо!"
        } else {
            imgTrophy.setBackgroundResource(R.drawable.sisyphus)
            tvCongrats.text = "Нет предела совершенству..."
        }

        val percent = "%.1f".format(ratio * 100)
        val scoreValue = "$percent%"
        tvScorePercent.text = scoreValue
    }
}