package com.example.muscletracking.model.musclepart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusclePart(
    @PrimaryKey
    @ColumnInfo(name = "musclepartid")
    val musclePartId:String,

    @ColumnInfo(name = "musclepartname")
    val musclePartName:String
)
