package com.example.muscletracking.dao.log

import androidx.room.*
import com.example.muscletracking.model.log.Log

@Dao
interface LogDao {

    @Query("SELECT * FROM log order by trainingdate desc")
    fun getAllLog(): List<Log>

    @Query("SELECT * FROM log WHERE menuname LIKE :menuName order by trainingdate desc, menuname")
    fun getLogByMenu(menuName: String): List<Log>

    @Query("SELECT * FROM log WHERE id = :logId")
    fun getLogById(logId: String): Log

    @Query("SELECT * FROM log WHERE trainingdate = :trainingDate")
    fun getLogByDate(trainingDate: String): List<Log>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLog(log: Log)

    @Update
    fun updateLog(log: Log)

    @Delete
    fun deleteLog(log: Log)

    @Query("DELETE FROM log")
    fun deleteAllLog()

}