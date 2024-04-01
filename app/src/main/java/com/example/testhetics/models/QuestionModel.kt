package com.example.testhetics.models

class QuestionModel(
    var question: String,
    var variants: ArrayList<String>,
    var correctAnswerIndex: Int
) {
    constructor() : this(
        "",
        arrayListOf("", "", "", ""),
        0)
}