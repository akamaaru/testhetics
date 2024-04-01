package com.example.testhetics.models

abstract class PracticeModel(
    var name: String,
    var description: String,
    var questions: ArrayList<QuestionModel>
) {
    val id: Int = hashCode()
    constructor() : this("", "", arrayListOf())
}