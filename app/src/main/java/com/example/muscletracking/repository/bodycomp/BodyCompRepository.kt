package com.example.muscletracking.repository.bodycomp

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.muscletracking.common.ApiResult
import com.example.muscletracking.common.apiCall
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.model.bodycomp.BodyCompResponse
import kotlinx.coroutines.flow.Flow

class BodyCompRepository(app: Application) {
    private val bodyCompDao = AppDatabase.getInstance(app).bodyCompDao()
    private val retrofitClient = RetrofitClient.getApiService()

    @WorkerThread
    suspend fun getLatestBodyComp(): BodyComp {
        return bodyCompDao.getLatestBodyComp()
    }

    @WorkerThread
    suspend fun insertBodyCompDb(bodyComp: BodyComp) {
        return bodyCompDao.insert(bodyComp)
    }

    @WorkerThread
    suspend fun updateBodyCompDb(bodyComp: BodyComp) {
        return bodyCompDao.update(bodyComp)
    }

    @WorkerThread
    suspend fun deleteAllBodyCompDb() {
        return bodyCompDao.deleteAll()
    }

    @WorkerThread
    suspend fun getAllBodyComp(userId: String): List<BodyCompResponse>? {
        val response = retrofitClient.getAllBodyComp(userId).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }

    fun getLatestBodyComp(userId: String): Flow<ApiResult<BodyCompResponse>> =
        apiCall { retrofitClient.getLatestBodyComp(userId) }

    @WorkerThread
    suspend fun insertBodyComp(
        height: Double,
        weight: Double,
        bfp: Double,
        bodyCompDate: String,
        userId: String
    ): BodyCompResponse? {
        val response = retrofitClient.addBodyComp(
            height.toString(),
            weight.toString(),
            bfp.toString(),
            bodyCompDate,
            userId
        ).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }

    @WorkerThread
    suspend fun updateBodyComp(
        bodyCompId: Int,
        height: Double,
        weight: Double,
        bfp: Double,
        userId: String
    ): BodyCompResponse? {
        val response = retrofitClient.updateBodyComp(
            bodyCompId,
            height.toString(),
            weight.toString(),
            bfp.toString(),
            userId
        ).execute()
        return if (response.isSuccessful) {
            return response.body()
        } else {
            null
        }
    }
}