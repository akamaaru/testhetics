package com.example.testhetics.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.utils.VariantsRecyclerViewInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton

class VariantAdapter(
    val variants: ArrayList<String>,
    private val variantsRecyclerViewInterface: VariantsRecyclerViewInterface
) : RecyclerView.Adapter<VariantAdapter.VariantViewHolder>() {
    private var selectedItem = 0

    class VariantViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.rb_option)
        val etVariant: EditText = itemView.findViewById(R.id.et_variant)
        val btnDelete: FloatingActionButton = itemView.findViewById(R.id.btn_delete_variant)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_question_variant, parent, false)

        return VariantViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return variants.size
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        val rbText = "${position + 1}."
        holder.radioButton.text = rbText
        holder.radioButton.isChecked = position == selectedItem

        holder.radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                selectedItem = holder.absoluteAdapterPosition
                variantsRecyclerViewInterface.onVariantCheck(position)
            }
        }

        holder.etVariant.setText(variants[position])
        holder.etVariant.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                variants[holder.absoluteAdapterPosition] = s.toString()
            }
        })

        holder.btnDelete.setOnClickListener {
            val minVariantNumber = 2
            if (variants.size == minVariantNumber) {
                Toast.makeText(
                    holder.btnDelete.context,
                    "Вопрос не может иметь менее $minVariantNumber вариантов!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (selectedItem == position) {
                selectedItem = 0
                variantsRecyclerViewInterface.onVariantCheck(0)
            } else if (selectedItem > position) {
                variantsRecyclerViewInterface.onVariantCheck(--selectedItem)
            }

            variantsRecyclerViewInterface.onVariantDelete(position)
        }
    }
}