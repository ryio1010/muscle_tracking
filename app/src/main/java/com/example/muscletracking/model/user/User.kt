package com.example.muscletracking.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userid")
    val userId:Int,

    @ColumnInfo(name = "username")
    val userName:String,

//    @ColumnInfo(name = "height")
//    val height:Double,
//
//    @ColumnInfo(name = "weight")
//    val weight:Double,
//
//    @ColumnInfo(name = "age")
//    val age:Int
)
