package com.example.testhetics.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.activities.PreparationActivity
import com.example.testhetics.models.QuizModel

class QuizAdapter(
    private val quizzes: ArrayList<QuizModel>,
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_card_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_card_description)
        var tvType: TextView = itemView.findViewById(R.id.tv_type)
        var practiceId = 0
        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PreparationActivity::class.java)
                intent.putExtra("practice_id", practiceId)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_practice, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val currentItem = quizzes[position]
        holder.tvName.text = currentItem.name
        holder.tvDescription.text = currentItem.description
        holder.tvType.text = "Квиз"
        holder.practiceId = currentItem.id
        Log.i("PRACTICE ID EXPLORE", holder.practiceId.toString())
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }
}