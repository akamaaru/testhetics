package com.example.testhetics.utils

interface QuestionsRecyclerViewInterface {
    fun onQuestionCheck(questionPosition: Int, variantPosition: Int)
    fun onQuestionDelete(questionPosition: Int)
    fun onSelectImage()
}