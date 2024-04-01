package com.example.testhetics.utils

import com.example.testhetics.adapters.ChoiceAdapter

interface VariantsRecyclerViewInterface {
    fun checkAnswer(choice: ChoiceAdapter.ChoiceViewHolder)
}