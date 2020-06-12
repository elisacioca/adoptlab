package com.example.adoptmypet.presentation.questionnaire

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Answer
import kotlinx.android.synthetic.main.item_answer.view.*

class QuestionnaireAdapter(
    private val answers: List<Answer>,
    private val clickAction: QuestionnaireInterface
) :
    RecyclerView.Adapter<QuestionnaireAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_answer,
                parent,
                false
            ),
            clickAction
        )
    }

    override fun getItemCount(): Int {
        return answers.count()
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(answers[position])
    }

    class VH(private val view: View, private val  clickAction: QuestionnaireInterface) : RecyclerView.ViewHolder(view) {
        fun bind(answer: Answer) {
            view.answerButton.text = answer.content
            view.setOnClickListener {
                clickAction.onItemClick()
            }
        }
    }

    interface QuestionnaireInterface {
        fun onItemClick()
    }

}