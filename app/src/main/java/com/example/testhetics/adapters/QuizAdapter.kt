package com.example.testhetics.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.activities.PreparationActivity
import com.example.testhetics.models.QuizModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QuizAdapter(
    private val QUIZ_KEY: String,
    private val quizzes: ArrayList<QuizModel>,
    private val isBtnDeleteVisible: Boolean
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {
    lateinit var databaseReference: DatabaseReference

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_card_name)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_card_description)
        var tvType: TextView = itemView.findViewById(R.id.tv_type)
        var btnDelete: FloatingActionButton = itemView.findViewById(R.id.btn_delete_quiz)
        var pbDeletion: ProgressBar = itemView.findViewById(R.id.pb_delete_quiz)
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
        databaseReference = FirebaseDatabase.getInstance().getReference(QUIZ_KEY)

        val currentItem = quizzes[position]
        holder.tvName.text = currentItem.name
        holder.tvDescription.text = currentItem.description
        holder.tvType.text = "Квиз"
        holder.practiceId = currentItem.id
        holder.btnDelete.visibility =
            if (isBtnDeleteVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
        holder.btnDelete.setOnClickListener {
            holder.pbDeletion.visibility = View.VISIBLE

            databaseReference.child(currentItem.id.toString()).removeValue().addOnCompleteListener {
                holder.pbDeletion.visibility = View.GONE

                if (it.isSuccessful) {
//                    quizzes.removeAt(position)
//                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }
}