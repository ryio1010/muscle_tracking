package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton = findViewById<Button>(R.id.btLogin)
        loginButton.setOnClickListener(LoginButtonListener())
    }

    private inner class LoginButtonListener: View.OnClickListener {
        override fun onClick(view: View) {
            val uidInputView = findViewById<TextView>(R.id.inputId)
            val userName = uidInputView.text.toString()

            val intent = Intent(this@MainActivity,TopActivity::class.java)
            intent.putExtra("userName",userName)
            startActivity(intent)
        }
    }
}