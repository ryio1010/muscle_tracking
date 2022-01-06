package com.example.muscletracking

import android.app.Application

class GlobalApplication : Application() {
    var _addTrainingMenu: MutableList<MutableMap<String, Any>> = mutableListOf()

    val muscleGroups = mutableListOf(
        "Chest",
        "Back",
        "Leg",
        "Arm",
        "Shoulder"
    )

    companion object {
        private var instance: GlobalApplication? = null

        fun getInstance(): GlobalApplication {
            if (instance == null) {
                instance = GlobalApplication()
            }
            return instance!!
        }
    }
}