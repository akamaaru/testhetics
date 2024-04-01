package com.example.testhetics.models

class QuizModel(
    name: String,
    description: String,
    questions: ArrayList<QuestionModel>
) : PracticeModel(name, description, questions) {
    constructor() : this("", "", arrayListOf())
}