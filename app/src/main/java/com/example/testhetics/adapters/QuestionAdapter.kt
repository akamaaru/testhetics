package com.example.testhetics.adapters

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.models.QuestionModel
import com.example.testhetics.utils.QuestionsRecyclerViewInterface
import com.example.testhetics.utils.VariantsRecyclerViewInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class QuestionAdapter(
    private val recyclerViewInterface: QuestionsRecyclerViewInterface,
    private val questions: ArrayList<QuestionModel>,
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    class QuestionViewHolder(
        val questions: ArrayList<QuestionModel>,
        private val recyclerViewInterface: QuestionsRecyclerViewInterface,
        itemView: View
    ) : RecyclerView.ViewHolder(itemView), VariantsRecyclerViewInterface {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_question_title)
        val etName: EditText = itemView.findViewById(R.id.et_question_name)
        var variants: ArrayList<String> = arrayListOf("", "", "", "")
        val rvVariants: RecyclerView = itemView.findViewById(R.id.rv_variants)
        val sbTime: SeekBar = itemView.findViewById(R.id.sb_time)
        val tvTime: TextView = itemView.findViewById(R.id.tv_new_time)
        val cardQuestion: CardView = itemView.findViewById(R.id.card_image)
        val ivQuestion: ImageView = itemView.findViewById(R.id.iv_question)
        val pbUpload: ProgressBar = itemView.findViewById(R.id.pb_upload_image)
        var variantAdapter: VariantAdapter = VariantAdapter(
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

        fun upload(imageUri: Uri) {
            pbUpload.visibility = View.VISIBLE
            val storageReference = FirebaseStorage
                .getInstance()
                .getReference(System.currentTimeMillis().toString())
            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener {
                        questions[absoluteAdapterPosition].image = it.toString()
                        pbUpload.visibility = View.GONE
                        ivQuestion.setImageURI(imageUri)
                        ivQuestion.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        ivQuestion.context,
                        "Ошибка при загрузке изображения!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    var currentImagePosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(
            questions,
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
                Toast.makeText(
                    holder.btnDeleteQuestion.context,
                    "Квиз не может быть без вопросов!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            recyclerViewInterface.onQuestionDelete(position)
        }

        holder.btnAddVariant.setOnClickListener {
            val maxVariantNumber = 6
            if (holder.variants.size == maxVariantNumber) {
                Toast.makeText(
                    holder.btnAddVariant.context,
                    "Вопрос не может превышать $maxVariantNumber вариантов!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            questions[position].variants.add("")
            holder.variantAdapter.notifyItemInserted(holder.variants.size - 1)
        }

        holder.rvVariants.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.variantAdapter = VariantAdapter(holder.variants, holder)
        holder.rvVariants.adapter = holder.variantAdapter

        val step = 5
        var trueTime = holder.sbTime.progress * step
        var tvTimeText = "${trueTime}с"
        holder.tvTime.text = tvTimeText

        holder.sbTime.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                trueTime = holder.sbTime.progress * step
                tvTimeText = "${trueTime}с"
                holder.tvTime.text = tvTimeText

                questions[holder.absoluteAdapterPosition].time = trueTime
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

//        if (questions[position].image.isNotEmpty()) {
////            TODO holder.ivQuestion.setImageURI()
//        }

        holder.cardQuestion.setOnClickListener {
            currentImagePosition = position
            recyclerViewInterface.onSelectImage()
        }
    }
}