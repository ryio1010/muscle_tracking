package com.example.muscletracking.dao.log

import androidx.room.*
import com.example.muscletracking.model.log.Log

@Dao
interface LogDao {

    @Query("SELECT * FROM log")
    fun getAllLog():List<Log>

    @Insert
    fun insertLog(log:Log)

    @Update
    fun updateLog(log:Log)

    @Delete
    fun deleteLog(log:Log)

}