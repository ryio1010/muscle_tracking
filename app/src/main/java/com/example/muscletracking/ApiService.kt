package com.example.muscletracking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("api/v1/log/{user_name}")
    suspend fun getLogInfo(
        @Path("user_name") userName:String
    ): Response<LogInfoResponse>
}

data class LogInfoResponse(
    val trainingLogs: List<TrainingLog>
)

data class TrainingLog(
    val userid:String,
    val traininglogid: Int,
    val menuid:Int,
    val trainingweight:Int,
    val trainingcount:Int,
    val trainingdate:String
)
