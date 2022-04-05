package com.example.muscletracking.repository.musclepart

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.model.musclepart.MusclePartResponse

class MusclePartRepository(app: Application) {

    private val musclePartDao = AppDatabase.getInstance(app).musclePartDao()
    private val retrofitClient = RetrofitClient.getApiService()

    @WorkerThread
    suspend fun getAllMusclePartFromDB(): List<MusclePart> {
        return musclePartDao.getAllMusclePart()
    }

    @WorkerThread
    suspend fun insertMusclePart(musclePart: MusclePart) {
        musclePartDao.insertMusclePart(musclePart)
    }

    @WorkerThread
    suspend fun deleteAllMusclePartOfDb() {
        musclePartDao.deleteAllMusclePartOfDb()
    }

    @WorkerThread
    suspend fun getAllMusclePart(): List<MusclePartResponse>? {
        val response = retrofitClient.getAllMusclePart().execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}