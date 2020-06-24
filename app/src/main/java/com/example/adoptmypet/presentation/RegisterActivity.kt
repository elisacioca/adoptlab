package com.example.adoptmypet.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.adoptmypet.R
import com.example.adoptmypet.api.ServiceFactory
import com.example.adoptmypet.models.User
import com.example.adoptmypet.presentation.dialogs.ErrorDialog
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)
    }

    fun onRegisterSubmit(view: View) {
        var name = tvName.text.toString()
        var email = tvEmail.text.toString()
        var password = tvPassword.text.toString()
        var repassword = tvRepassword.text.toString()

        if (password == repassword && password.length > 0 && repassword.length > 0) {
            var user = User(username = email, password = password, name = name, token = "")
            if (name.length >= 3 && email.length > 10 && password.length > 8
                && email.isValidEmail()) {
                ServiceFactory.service.addUser(user).enqueue(
                    object : Callback<User> {
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            val dialog: ErrorDialog = ErrorDialog
                            dialog.show(supportFragmentManager, "Error")
                            Log.e("MainActivity", t.localizedMessage)
                        }

                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                response.body().let { userReceived ->
                                    if (userReceived != null) {
                                        goToNextPage(userReceived)
                                    };
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    resources.getString(R.string.register_error), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    });
            }
            else {
                var error: String = ""
                if (name.length < 3)
                    error = error + "Numele introdus este prea scurt" + "/n"
                if (email.length <= 10 || !email.isValidEmail())
                    error = error + "Emailul introdus nu pare a fi valid" + "/n"
                if (password.length <= 8)
                    error = error + "Parola trebuie sa contina minim 9 caractere" + "/n"

                Toast.makeText(
                    applicationContext,
                    error, Toast.LENGTH_SHORT
                ).show()
            }
        }
        else {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.password_mismatch), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun goToNextPage(user : User) {
        val intent = Intent(this, WelcomeActivity::class.java)
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("token",user.token)
        editor.commit()
        intent.putExtra("user_name", user.name)
        intent.putExtra("user_email", user.username)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
