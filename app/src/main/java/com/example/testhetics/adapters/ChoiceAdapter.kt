package com.example.testhetics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.utils.VariantsRecyclerViewInterface

class ChoiceAdapter(
    private val context: Context,
    private val recyclerViewInterface: VariantsRecyclerViewInterface,
    private val variants: ArrayList<String>,
    private val correctAnswerIndex: Int
) : RecyclerView.Adapter<ChoiceAdapter.ChoiceViewHolder>() {
    class ChoiceViewHolder(
        private val context: Context,
        private val recyclerViewInterface: VariantsRecyclerViewInterface,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val btnChoice: Button = itemView.findViewById(R.id.btn_choice)
        var isCorrect = true
        private var isChosen = false

        init {
            btnChoice.setOnClickListener {
                isChosen = true
                recyclerViewInterface.checkAnswer(this)
            }
        }

        fun colorizeAnswered() {
            if (isChosen || isCorrect) {
                colorize()
            }
        }

        fun colorize() {
            if (isCorrect) {
                btnChoice.backgroundTintList = ContextCompat.getColorStateList(
                    context,
                    R.color.button_correct_tint
                )
            } else {
                btnChoice.backgroundTintList = ContextCompat.getColorStateList(
                    context,
                    R.color.button_incorrect_tint
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choice, parent, false)
        return ChoiceViewHolder(
            context,
            recyclerViewInterface,
            view
        )
    }

    override fun getItemCount(): Int {
        return variants.size
    }

    override fun onBindViewHolder(holder: ChoiceViewHolder, position: Int) {
        val btnChoiceText = "${(position + 'A'.code).toChar()}. ${variants[position]}"

        holder.btnChoice.text = btnChoiceText
        holder.isCorrect = position == correctAnswerIndex
    }
}