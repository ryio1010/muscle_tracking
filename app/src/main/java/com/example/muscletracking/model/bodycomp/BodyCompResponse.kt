package com.example.muscletracking.model.bodycomp

data class BodyCompResponse(
    val bodyCompId: Int,
    val height: Double,
    val weight: Double,
    val bfp: Double,
    val bmi: Double,
    val lbm: Double,
    val bodyCompDate: String
)