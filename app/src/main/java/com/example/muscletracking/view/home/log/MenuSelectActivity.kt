package com.example.muscletracking.view.home.log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.example.muscletracking.R
import com.example.muscletracking.view.common.ToolBarCustomView
import com.example.muscletracking.view.common.ToolBarCustomViewDelegate

class MenuSelectActivity : AppCompatActivity(), ToolBarCustomViewDelegate {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        setCustomBar()

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = TrainingPartListFragment()
        transaction.add(R.id.fcvTrainingMenuSelect, fragment)
        transaction.commit()
    }

    private fun setCustomBar() {
        val toolBarCustomView = ToolBarCustomView(this)
        toolBarCustomView.delegate = this

        val title = getString(R.string.label_training_muscle_groups)
        toolBarCustomView.configure(title, isHideLeftButton = false, isHideRightButton = false)

        val layout = findViewById<LinearLayout>(R.id.llAppBarMenuSelect)
        layout.addView(toolBarCustomView)
    }

    override fun onClickedLeftButton() {
        finish()
    }

    override fun onClickedRightButton() {
        finish()
    }
}