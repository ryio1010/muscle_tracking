package com.example.muscletracking.config.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("user/{username}")
    fun getUserByName(
        @Path("username") userName:String
    ): Call<User>
}

data class User(
    val id:Int,
    val userName:String
)