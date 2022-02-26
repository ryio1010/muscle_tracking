package com.example.muscletracking.dao.musclepart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.muscletracking.model.musclepart.MusclePart

@Dao
interface MusclePartDao {

    @Query("SELECT * FROM musclepart")
    fun getAllMusclePart():List<MusclePart>

    @Insert
    fun insertMusclePart(musclePart: MusclePart)
}