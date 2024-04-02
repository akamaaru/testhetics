package com.example.testhetics.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.testhetics.activities.CreateQuizActivity
import com.example.testhetics.R

class CreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        val btnCreateQuiz = view.findViewById<Button>(R.id.btn_create_quiz)
        val btnCreateTest = view.findViewById<Button>(R.id.btn_create_test)
        val btnCreatePoll = view.findViewById<Button>(R.id.btn_create_poll)

        btnCreateTest.isEnabled = false
        btnCreatePoll.isEnabled = false

        btnCreateQuiz.setOnClickListener {
            val intent = Intent(it.context, CreateQuizActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}