package com.example.muscletracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muscletracking.view.home.log.TrainingPartListFragment

class MenuSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_select)

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = TrainingPartListFragment()
        transaction.add(R.id.fcvTrainingMenuSelect, fragment)
        transaction.commit()
    }
}