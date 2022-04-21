package com.example.muscletracking.view.home.logdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.muscletracking.R
import com.example.muscletracking.view.common.ToolBarCustomView

class LogDetailActivity : AppCompatActivity() {
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

        val title = getString(R.string.label_log_detail)
        toolBarCustomView.configure(title, isHideLeftButton = false, isHideRightButton = false)

        val layout = findViewById<LinearLayout>(R.id.llAppBarLogDetail)
        layout.addView(toolBarCustomView)
    }
}