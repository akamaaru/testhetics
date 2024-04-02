package com.example.testhetics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testhetics.R
import com.example.testhetics.adapters.QuizAdapter
import com.example.testhetics.models.QuizModel
import com.example.testhetics.utils.MarginItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    lateinit var QUIZ_KEY: String
    // TODO: обобщить для всех типов
    lateinit var list: ArrayList<QuizModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val tvAccountLabel = view?.findViewById<TextView>(R.id.tv_account_info)
        tvAccountLabel?.text = FirebaseAuth.getInstance().currentUser?.email

        QUIZ_KEY = getString(R.string.QUIZ_KEY)
        val databaseReference = FirebaseDatabase.getInstance().getReference(QUIZ_KEY)
        list = arrayListOf()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_profile_quizzes)!!
        recyclerView.addItemDecoration(MarginItemDecoration(20))
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val model = dataSnapshot.getValue(QuizModel::class.java)
                        if (model != null) {
                            list.add(model)
                        }
                    }

                    recyclerView.adapter = QuizAdapter(QUIZ_KEY, list, true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        return view
    }
}