package com.example.muscletracking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

class LogTrainingActivity : AppCompatActivity() {
    private val muscleGroups = mutableListOf(
        "Chest",
        "Back",
        "Leg",
        "Arm",
        "Shoulder"
    )

    private var chestTrainingList = mutableListOf(
        "chest",
        "aaaa",
        "bbbb",
        "cccc"
    )

    private var backTrainingList = mutableListOf(
        "back",
        "dddd",
        "eeee",
        "ffff"
    )

    private var legTrainingList = mutableListOf(
        "leg",
        "gggg",
        "hhhh",
        "iiii"
    )

    private var armTrainingList = mutableListOf(
        "arm",
        "jjjj",
        "kkkk",
        "llll"
    )

    private var shoulderTrainingList = mutableListOf(
        "shoulder",
        "mmmm",
        "nnnn",
        "oooo"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_training)


        // set spinner of muscle groups
        val spinnerMuscleGroups = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val adapterMuscleGroups =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, muscleGroups)
        adapterMuscleGroups.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerMuscleGroups.adapter = adapterMuscleGroups
        spinnerMuscleGroups.onItemSelectedListener = spinnerSelectedMuscleGroupsListener()


        // set spinner of training menus
        val spinnerTrainingMenus = findViewById<Spinner>(R.id.spinnerTrainingMenus)
        val adapterTrainingMenus =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, chestTrainingList)
        adapterTrainingMenus.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinnerTrainingMenus.adapter = adapterTrainingMenus
    }

    private inner class spinnerSelectedMuscleGroupsListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val spinner = parent as Spinner
            val selectedMuscleGroups = spinner!!.selectedItem.toString()
            Log.d("test", "spinnertest" + selectedMuscleGroups)

            var trainingList = when (selectedMuscleGroups) {
                "Chest" -> chestTrainingList
                "Back" -> backTrainingList
                "Leg" -> legTrainingList
                "Arm" -> armTrainingList
                "Shoulder" -> shoulderTrainingList
                else -> chestTrainingList
            }

            val spinnerTrainingMenus = findViewById<Spinner>(R.id.spinnerTrainingMenus)
            val adapterTrainingMenus =
                ArrayAdapter(this@LogTrainingActivity, android.R.layout.simple_spinner_item, trainingList)
            adapterTrainingMenus.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinnerTrainingMenus.adapter = adapterTrainingMenus
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}