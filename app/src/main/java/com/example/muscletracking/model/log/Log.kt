package com.example.muscletracking.model.log

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Log(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val logId: Int,

    @ColumnInfo(name = "menuid")
    val menuId: Int,

    @ColumnInfo(name = "menuname")
    val menuName: String,

    @ColumnInfo(name = "musclepart")
    val musclePart : String,

    @ColumnInfo(name = "trainingweight")
    val trainingWeight: Double,

    @ColumnInfo(name = "trainingcount")
    val trainingCount: Int,

    @ColumnInfo(name = "trainingdate")
    val trainingDate: String,

    @ColumnInfo(name = "trainingmemo")
    val trainingMemo: String
)
