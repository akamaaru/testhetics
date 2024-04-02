package com.example.testhetics.models

class QuizModel(
    name: String,
    description: String,
    questions: ArrayList<QuestionModel>,
    author: String
) : PracticeModel(name, description, questions, author) {
    constructor() : this("", "", arrayListOf(), "")
}