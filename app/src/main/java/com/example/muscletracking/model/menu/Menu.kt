package com.example.muscletracking.model.menu

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Menu(
    @PrimaryKey
    @ColumnInfo(name = "menuid")
    val menuId: Int,

    @ColumnInfo(name = "menuname")
    val menuName: String,

    @ColumnInfo(name = "musclepart")
    val musclePart: String,
)