package com.example.muscletracking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.muscletracking.view.home.LogWatchFragment

class LogDetailActivity : AppCompatActivity(),ToolBarCustomViewDelegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_detail)

        setCustomBar()

        val logId = intent.getStringExtra("logId")
        val bundle = Bundle()
        bundle.putString("logId", logId)

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = LogWatchFragment()
        fragment.arguments = bundle
        transaction.add(R.id.fcvLogDetail, fragment)
        transaction.commit()
    }

    private fun setCustomBar() {
        val toolBarCustomView = ToolBarCustomView(this)
        toolBarCustomView.delegate = this

        val title = getString(R.string.label_training_muscle_groups)
        toolBarCustomView.configure(title, isHideLeftButton = false, isHideRightButton = false)

        val layout = findViewById<LinearLayout>(R.id.llAppBarLogDetail)
        layout.addView(toolBarCustomView)
    }

    override fun onClickedLeftButton() {
        finish()
    }

    override fun onClickedRightButton() {
        finish()
    }
}