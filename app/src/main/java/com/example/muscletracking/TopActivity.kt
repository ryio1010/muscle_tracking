package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

class TopActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top)

        // set userid
        val labelUserName = findViewById<TextView>(R.id.labelUserName)
        val userName = intent.getStringExtra("userName")
        labelUserName.text = "userName : " + userName

        // set listener to logButton
        val logButton = findViewById<Button>(R.id.btLog)
        val logWatchButton = findViewById<Button>(R.id.btShowHistory)
        logButton.setOnClickListener(LogButtonListener())
        logWatchButton.setOnClickListener(LogButtonListener())
    }

    private inner class LogButtonListener: View.OnClickListener {
        override fun onClick(view: View) {
           when(view.id) {
               R.id.btLog -> {
                   val intent = Intent(this@TopActivity,LogTrainingActivity::class.java)
                   startActivity(intent)
               }
               R.id.btShowHistory -> {
                   val intent = Intent(this@TopActivity,LogWatchActivity::class.java)
                   startActivity(intent)
               }
               else -> {

               }
           }
        }
    }
}