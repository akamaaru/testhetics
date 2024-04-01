package com.example.testhetics.models

class QuestionModel(
    var question: String,
    val variants: ArrayList<String>,
    var correctAnswerIndex: Int
) {
    constructor() : this(
        "",
        arrayListOf("", "", "", ""),
        0)
}