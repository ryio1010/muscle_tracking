package com.example.muscletracking.model.log

data class LogResponse(
    val logId: Int,
    val menuName:String,
    val trainingWeight:Double,
    val trainingCount: Int,
    val trainingDate:String,
)
