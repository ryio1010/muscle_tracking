package com.example.muscletracking.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "userid")
    val userId: String,

    @ColumnInfo(name = "username")
    val userName: String,

    @ColumnInfo(name = "password")
    val password: String
)
