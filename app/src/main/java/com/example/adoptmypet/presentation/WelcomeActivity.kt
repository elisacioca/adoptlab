package com.example.adoptmypet.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.adoptmypet.*
import com.example.adoptmypet.presentation.questionnaire.QuestionnaireActivity
import com.example.adoptmypet.utils.ADOPTER
import com.example.adoptmypet.utils.ADOPTING
import com.example.adoptmypet.utils.EXTRA_USER_EMAIL
import com.example.adoptmypet.utils.EXTRA_USER_ROLE
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_welcome)
        val name = intent.getStringExtra("user_name")
        username = intent.getStringExtra("user_email")
        welcome.text = "Bine ai venit " + name + "!";
    }

    fun onAdoptingSubmit(view: View) {
        val intent = Intent(this, QuestionnaireActivity::class.java)
        intent.putExtra(
            EXTRA_USER_ROLE,
            ADOPTING
        )
        intent.putExtra(EXTRA_USER_EMAIL, username)
        startActivity(intent)
    }
    fun onAdopterSubmit(view: View) {
        val intent = Intent(this, QuestionnaireActivity::class.java)
        intent.putExtra(
            EXTRA_USER_ROLE,
            ADOPTER
        )
        intent.putExtra(EXTRA_USER_EMAIL, username)
        startActivity(intent)
    }
}