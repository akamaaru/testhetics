package com.example.testhetics.models

class QuestionModel(
    var question: String,
    var variants: ArrayList<String>,
    var correctAnswerIndex: Int,
    var time: Int,
    var image: String
) {
    constructor() : this(
        "",
        arrayListOf("", "", "", ""),
        0,
        10,
        ""
    )
}