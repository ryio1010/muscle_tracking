package com.example.muscletracking.view.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.muscletracking.MainActivity
import com.example.muscletracking.R

class RegisterUserCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user_complete)

        val loginButton = findViewById<Button>(R.id.btToLogin)
        loginButton.setOnClickListener(LoginButtonListener())
    }

    private inner class LoginButtonListener: View.OnClickListener {
        override fun onClick(view: View?) {
            val intent =
                Intent(this@RegisterUserCompleteActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}