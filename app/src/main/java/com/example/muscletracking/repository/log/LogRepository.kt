package com.example.muscletracking.repository.log

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.model.log.LogResponse

class LogRepository(app: Application) {
    private val logDao = AppDatabase.getInstance(app).logDao()
    private val retrofitClient = RetrofitClient.getApiService()

    @WorkerThread
    suspend fun getAllLogFromDB(): List<Log> {
        return logDao.getAllLog()
    }

    @WorkerThread
    suspend fun getLogByMenu(menuName: String): List<Log> {
        return logDao.getLogByMenu(menuName)
    }

    @WorkerThread
    suspend fun getLogByDate(trainingDate: String): List<Log> {
        return logDao.getLogByDate(trainingDate)
    }

    @WorkerThread
    suspend fun insertLogOfDB(log: Log) {
        return logDao.insertLog(log)
    }

    @WorkerThread
    suspend fun updateLogOfDB(log: Log) {
        return logDao.updateLog(log)
    }

    @WorkerThread
    suspend fun deleteLogOfDB(log: Log) {
        return logDao.deleteLog(log)
    }

    @WorkerThread
    suspend fun getAllLog(userId: String): List<LogResponse>? {
        val response = retrofitClient.getAllLog(userId).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }

    @WorkerThread
    suspend fun insertLog(
        menuId: String,
        menuName: String,
        trainingWeight: String,
        trainingCount: String,
        trainingDate: String,
        userId: String
    ): LogResponse? {
        val response =
            retrofitClient.addLog(
                menuId,
                menuName,
                trainingWeight,
                trainingCount,
                trainingDate,
                userId
            )
                .execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }

    @WorkerThread
    suspend fun updateLog(
        logId: String,
        menuId: String,
        menuName: String,
        trainingWeight: String,
        trainingCount: String,
        trainingDate: String,
        userId: String
    ): LogResponse? {
        val response = retrofitClient.updateLog(
            logId,
            menuId,
            menuName,
            trainingWeight,
            trainingCount,
            trainingDate,
            userId
        ).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }

    @WorkerThread
    suspend fun deleteLog(logId: String): Boolean? {
        val response = retrofitClient.deleteLog(logId).execute()
        return response.body()
    }

}