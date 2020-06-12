package com.example.adoptmypet.presentation.questionnaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.EXTRA_USER_ROLE
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Answer
import kotlinx.android.synthetic.main.activity_questionnaire.*

class QuestionnaireActivity : AppCompatActivity(),
    QuestionnaireAdapter.QuestionnaireInterface {
    private var questionNumber = 0;
    private var listOfAnswers = emptyList<Answer>()
    lateinit var adapter: QuestionnaireAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(QuestionnaireViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_questionnaire)
        val user_role = intent.getBooleanExtra(EXTRA_USER_ROLE, true)
        adapter = QuestionnaireAdapter(listOfAnswers, this)
        rvQuestionnaire.adapter = adapter
        viewModel.getQuestions(user_role)
        observeEvents()

    }

    private fun observeEvents() {
        viewModel.listOfQuestions.observe(this, Observer {
            if (it[0].answers != null) {
                listOfAnswers = it[0].answers ?: emptyList()
                adapter.notifyDataSetChanged()
            }

        })
    }


    override fun onItemClick() {
        questionNumber++
        if (questionNumber <= viewModel.listOfQuestions.value!!.count()) {
            if (viewModel.listOfQuestions.value.isNullOrEmpty().not()) {
                listOfAnswers = viewModel.listOfQuestions.value!![questionNumber].answers!!
                adapter.notifyDataSetChanged()
            }
        }
    }
}
