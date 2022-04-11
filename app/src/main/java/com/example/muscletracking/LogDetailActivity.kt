package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muscletracking.view.home.LogWatchFragment

class LogDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_detail)

        val logId = intent.getStringExtra("logId")
        val bundle = Bundle()
        bundle.putString("logId", logId)

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = LogWatchFragment()
        fragment.arguments = bundle
        transaction.add(R.id.fcvLogDetail, fragment)
        transaction.commit()
    }
}