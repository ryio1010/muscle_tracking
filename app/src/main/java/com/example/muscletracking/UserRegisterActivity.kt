package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class UserRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        val registerUserButton = findViewById<Button>(R.id.btRegisterUser)
        registerUserButton.setOnClickListener(RegisterUserButtonListener())
    }

    private inner class RegisterUserButtonListener : View.OnClickListener {
        override fun onClick(view: View?) {
            val registerUserIdComponent = findViewById<TextView>(R.id.inputRegisterUserId)
            val registerUserPwComponent = findViewById<TextView>(R.id.inputRegisterUserPw)

            val registerUserId = registerUserIdComponent.text.toString()
            val registerUserPw = registerUserPwComponent.text.toString()

            if (false){
                // api request
                val client: OkHttpClient = OkHttpClient()
                val url: String = "http://0.0.0.0:8080/api/register/$registerUserId"
                val request: Request = Request.Builder().url(url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        Log.d("debug", response.body!!.string())
                        val intent =
                            Intent(this@UserRegisterActivity, RegisterUserCompleteActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("debug", e.toString())
                    }
                })
            }
            val intent =
                Intent(this@UserRegisterActivity, RegisterUserCompleteActivity::class.java)
            startActivity(intent)

        }
    }
}