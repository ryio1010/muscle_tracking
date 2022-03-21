package com.example.muscletracking.model.bodycomp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BodyComp(
    @PrimaryKey
    @ColumnInfo(name = "bodycompid")
    val bodyCompId: Int,

    @ColumnInfo(name = "height")
    val height: Double,

    @ColumnInfo(name = "weight")
    val weight: Double,

    @ColumnInfo(name = "bfp")
    val bfp: Double,

    @ColumnInfo(name = "bmi")
    val bmi: Double,

    @ColumnInfo(name = "lbm")
    val lbm: Double,

    @ColumnInfo(name = "date")
    val bodyCompDate: String
)
