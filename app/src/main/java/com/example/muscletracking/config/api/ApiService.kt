package com.example.muscletracking.config.api

import com.example.muscletracking.model.user.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("user/{username}")
    fun getUserByName(
        @Path("username") userName: String
    ): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("userid") userId: String,
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<Boolean>

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("userid") userid: String,
        @Field("password") password: String
    ): Call<UserResponse>
}
