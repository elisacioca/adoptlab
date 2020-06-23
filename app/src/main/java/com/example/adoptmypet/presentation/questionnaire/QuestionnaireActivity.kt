package com.example.adoptmypet.presentation.questionnaire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.utils.EXTRA_USER_ROLE
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Answer
import com.example.adoptmypet.presentation.feed.FeedActivity
import com.example.adoptmypet.presentation.PetDescriptionActivity
import com.example.adoptmypet.utils.ADOPTER
import kotlinx.android.synthetic.main.activity_questionnaire.*

class QuestionnaireActivity : AppCompatActivity(),
    QuestionnaireAdapter.QuestionnaireInterface {
    private var questionNumber = 0;
    private var affection = 0;
    private var freedom = 0;
    private var factorRisk = 0;
    private var userRole = true;
    private var questionnaire = "";
    private var animalType = 0;
    private var age: String? = "";
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

    private fun populateExtras(answer: Answer, questionNumber: Int){
        questionnaire = questionnaire + viewModel.listOfQuestions.value!![questionNumber-1].questionText + ":" + answer.content + "\n" ;
        if (questionNumber == 1) {
            animalType = if (answer.content=="O pisica") 0 else 1
        }
        if (userRole == ADOPTER && questionNumber == 2) {
            age = answer.content;
        }
        affection = affection + answer.affection!!
        freedom = freedom + answer.freedom!!
        factorRisk = factorRisk + answer.factorRisk!!
    }

    override fun onItemClick(answer: Answer) {
        questionNumber++
        populateExtras(answer, questionNumber);

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
            Toast.makeText(
                this,
                "Done, Affection: " + affection + ", Freedom: " + freedom + " FactorRisk: " + factorRisk,
                Toast.LENGTH_LONG
            ).show()
            if (!userRole) {
                val intent = Intent(this, FeedActivity::class.java)
                intent.putExtra("affection", affection)
                intent.putExtra("freedom", freedom)
                intent.putExtra("factorRisk", factorRisk)
                intent.putExtra("questionnaire", questionnaire)
                intent.putExtra("animalType", animalType)
                startActivity(intent)
            } else {
                val intent = Intent(this, PetDescriptionActivity::class.java)
                intent.putExtra("affection", affection)
                intent.putExtra("freedom", freedom)
                intent.putExtra("factorRisk", factorRisk)
                intent.putExtra("questionnaire", questionnaire)
                intent.putExtra("animalType", animalType)
                intent.putExtra("age", age)
                startActivity(intent)
            }
        }
    }
}
