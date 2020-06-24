package com.example.adoptmypet.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adoptmypet.R
import com.example.adoptmypet.api.ServiceFactory
import com.example.adoptmypet.models.User
import com.example.adoptmypet.presentation.dialogs.ErrorDialog
import com.example.adoptmypet.utils.service
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)

        logoImage.animation = AnimationUtils.loadAnimation(
            this,
            R.anim.top_fade_animation
        )

        Handler().postDelayed(this::verifyLogin, 2500)

        butttonsLayout.animation = AnimationUtils.loadAnimation(
            this,
            R.anim.fade_animation
        )

        registerReference.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyLogin() {
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        if(sharedPreference.getString("token", "") != "") {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun onLoginSubmit(view: View) {
        var username: String = tvEmail.text.toString()
        var password: String = tvPassword.text.toString()
        var user = com.example.adoptmypet.entities.User(username, password)
        ServiceFactory.service.loginUser(user).enqueue(
            object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("MainActivity", t.localizedMessage)
                    val dialog: ErrorDialog = ErrorDialog
                    dialog.show(supportFragmentManager, "Error")
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        response.body().let { userReceived ->
                            if (userReceived != null) {
                                goToNextPage(userReceived)
                            };
                        }
                    }
                    if (response.code() == 401) {
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.error_on_login), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            });
    }

    private fun goToNextPage(user: User) {
        val intent = Intent(this, WelcomeActivity::class.java)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token", user.token)
        editor.putString("username", user.username)
        editor.putString("name", user.name)
        editor.commit()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
    }


}