package com.example.muscletracking.config.api

import com.example.muscletracking.model.user.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /**
     * 新規ユーザー登録API
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("userid") userId: String,
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<Boolean>

    /**
     * ユーザーログインAPI
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("userid") userid: String,
        @Field("password") password: String
    ): Call<UserResponse>

    /**
     * ユーザー情報更新API
     */
    @FormUrlEncoded
    @PUT("user")
    fun updateUserInfo(
        @Field("userid") userid: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("height") height: String,
        @Field("weight") weight: String
    ): Call<UserResponse>
}
