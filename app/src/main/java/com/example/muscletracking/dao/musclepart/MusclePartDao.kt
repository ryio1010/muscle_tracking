package com.example.muscletracking.dao.musclepart

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.muscletracking.model.musclepart.MusclePart

@Dao
interface MusclePartDao {

    @Query("SELECT * FROM musclepart")
    fun getAllMusclePart():List<MusclePart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusclePart(musclePart: MusclePart)
}