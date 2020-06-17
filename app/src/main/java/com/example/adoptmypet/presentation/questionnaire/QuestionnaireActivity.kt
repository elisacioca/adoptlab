package com.example.adoptmypet.presentation.questionnaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.EXTRA_USER_ROLE
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Answer
import kotlinx.android.synthetic.main.activity_questionnaire.*

class QuestionnaireActivity : AppCompatActivity(),
    QuestionnaireAdapter.QuestionnaireInterface {
    private var questionNumber = 0;
    private var affection = 0;
    private var freedom = 0;
    private var factorRisk = 0;
    private var userRole = true;
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
        userRole = intent.getBooleanExtra(EXTRA_USER_ROLE, true)
        adapter = QuestionnaireAdapter(viewModel.listOfAnswers, this)
        rvQuestionnaire.adapter = adapter
        viewModel.getQuestions(userRole)
        observeEvents()

    }

    private fun observeEvents() {
        viewModel.listOfQuestions.observe(this, Observer {
            if (it[0].answers != null) {
                question_number.text =
                    getString(
                        R.string.page_number_ex,
                        questionNumber + 1,
                        viewModel.listOfQuestions.value?.count() ?: 0
                    )

                viewModel.listOfAnswers.clear()
                viewModel.listOfAnswers.addAll(
                    ((it[0].answers ?: emptyList()) as MutableList<Answer>)
                )
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.questionNumber.observe(this, Observer {
            question_number.text =
                getString(
                    R.string.page_number_ex,
                    it,
                    viewModel.listOfQuestions.value?.count() ?: 0
                )
        })
        viewModel.question.observe(this, Observer {
            question.text = it
        })
    }

    override fun onItemClick(answer: Answer) {
        questionNumber++
        affection = affection + answer.affection!!
        freedom = freedom + answer.freedom!!
        factorRisk = factorRisk + answer.factorRisk!!

        if (questionNumber < viewModel.listOfQuestions.value!!.count()) {
            if (viewModel.listOfQuestions.value.isNullOrEmpty().not()) {
                val question = viewModel.listOfQuestions.value!![questionNumber]
                viewModel.question.value = question.questionText
                viewModel.questionNumber.value = questionNumber + 1

                viewModel.listOfAnswers.clear()
                viewModel.listOfAnswers.addAll(
                    question.answers!!.toMutableList()
                )
                adapter.notifyDataSetChanged()
            }
        } else if (questionNumber == viewModel.listOfQuestions.value!!.count()) {
            Toast.makeText(this, "Done, Affection: "+affection+", Freedom: "+freedom+" FactorRisk: "+factorRisk, Toast.LENGTH_LONG).show()
            if (userRole)
            {

            }
            else
            {

            }
        }
    }
}
