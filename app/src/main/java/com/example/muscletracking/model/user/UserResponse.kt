package com.example.muscletracking.model.user

data class UserResponse(
    val userid:String,
    val username:String,
    val password:String,
    val height:Double,
    val weight:Double
)
