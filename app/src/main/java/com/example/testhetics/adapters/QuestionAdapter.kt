package com.example.testhetics.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.models.QuestionModel
import com.example.testhetics.utils.QuestionsRecyclerViewInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestionAdapter(
    private val recyclerViewInterface: QuestionsRecyclerViewInterface,
    private val questions: ArrayList<QuestionModel>,
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    class QuestionViewHolder(
        recyclerViewInterface: QuestionsRecyclerViewInterface,
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_question_title)
        val etName: EditText = itemView.findViewById(R.id.et_question_name)
        val rgCorrectAnswer: RadioGroup = itemView.findViewById(R.id.rg_correct_answer)

        val etVariant1: EditText = itemView.findViewById(R.id.et_variant_1)
        val etVariant2: EditText = itemView.findViewById(R.id.et_variant_2)
        val etVariant3: EditText = itemView.findViewById(R.id.et_variant_3)
        val etVariant4: EditText = itemView.findViewById(R.id.et_variant_4)
        val etVariants = arrayListOf(
            etVariant1,
            etVariant2,
            etVariant3,
            etVariant4
        )

        private var btnDelete: FloatingActionButton =
            itemView.findViewById(R.id.btn_delete_question)

        init {
            btnDelete.setOnClickListener {
                recyclerViewInterface.onItemDelete(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(
            recyclerViewInterface,
            view
        )
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val title = "Вопрос №${position + 1}"
        holder.tvTitle.text = title

        holder.etName.setText(questions[position].question)
        holder.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                questions[holder.absoluteAdapterPosition].question = s.toString()
            }
        })

        for (i in 0..<holder.etVariants.size) {
            holder.etVariants[i].setText(questions[position].variants[i])
            holder.etVariants[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    questions[holder.absoluteAdapterPosition].variants[i] = s.toString()
                }
            })
        }

        val rbCorrectId = holder.rgCorrectAnswer.children.find {
            val index =
                holder.rgCorrectAnswer.indexOfChild(holder.rgCorrectAnswer.findViewById(it.id))
            return@find index == questions[position].correctAnswerIndex
        }!!.id
        holder.rgCorrectAnswer.check(rbCorrectId)
        holder.rgCorrectAnswer.setOnCheckedChangeListener { group, checkedId ->
            val correctAnswerIndex: Int = group.indexOfChild(group.findViewById(checkedId))
            questions[holder.absoluteAdapterPosition].correctAnswerIndex = correctAnswerIndex
        }
    }
}