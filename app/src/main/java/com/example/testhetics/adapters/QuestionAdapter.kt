package com.example.testhetics.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.models.QuestionModel
import com.example.testhetics.utils.QuestionsRecyclerViewInterface
import com.example.testhetics.utils.VariantsRecyclerViewInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton

class QuestionAdapter(
    private val context: Context,
    private val recyclerViewInterface: QuestionsRecyclerViewInterface,
    private val questions: ArrayList<QuestionModel>,
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    class QuestionViewHolder(
        val context: Context,
        val recyclerViewInterface: QuestionsRecyclerViewInterface,
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), VariantsRecyclerViewInterface {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_question_title)
        val etName: EditText = itemView.findViewById(R.id.et_question_name)
        var variants: ArrayList<String> = arrayListOf("", "", "", "")
        val rvVariants: RecyclerView = itemView.findViewById(R.id.rv_variants)
        var variantAdapter: VariantAdapter = VariantAdapter(
            context,
            variants,
            this
        )

        var btnDeleteQuestion: FloatingActionButton =
            itemView.findViewById(R.id.btn_delete_question)
        var btnAddVariant: FloatingActionButton =
            itemView.findViewById(R.id.btn_add_variant)

        override fun onVariantCheck(position: Int) {
            for (i in 0..<rvVariants.childCount) {
                val holder: VariantAdapter.VariantViewHolder =
                    rvVariants.findViewHolderForAdapterPosition(i) as VariantAdapter.VariantViewHolder
                holder.radioButton.isChecked = i == position
            }

            recyclerViewInterface.onQuestionCheck(absoluteAdapterPosition, position)
        }

        override fun onVariantDelete(position: Int) {
            variants.removeAt(position)
            variantAdapter.notifyItemRemoved(position)

            for (i in position..<variants.size) {
                variantAdapter.notifyItemChanged(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(
            context,
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
        holder.variants = questions[position].variants
        holder.variantAdapter.notifyItemRangeChanged(0, holder.variants.size)

        holder.etName.setText(questions[position].question)
        holder.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                questions[holder.absoluteAdapterPosition].question = s.toString()
            }
        })

        holder.btnDeleteQuestion.setOnClickListener {
            if (questions.size == 1) {
                Toast.makeText(context, "Квиз не может быть без вопросов!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            recyclerViewInterface.onQuestionDelete(position)
        }

        holder.btnAddVariant.setOnClickListener {
            val maxVariantNumber = 6
            if (holder.variants.size == maxVariantNumber) {
                Toast.makeText(
                    context,
                    "Вопрос не может превышать $maxVariantNumber вариантов!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            questions[position].variants.add("")
            holder.variantAdapter.notifyItemInserted(holder.variants.size - 1)
        }

        holder.rvVariants.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.variantAdapter = VariantAdapter(context, holder.variants, holder)
        holder.rvVariants.adapter = holder.variantAdapter
    }
}