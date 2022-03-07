package com.example.muscletracking.config.api

import com.example.muscletracking.model.log.LogResponse
import com.example.muscletracking.model.menu.MenuResponse
import com.example.muscletracking.model.musclepart.MusclePartResponse
import com.example.muscletracking.model.user.UserResponse
import retrofit2.Call
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

    /**
     * トレーニングメニュー取得API
     */
    @GET("menu/{userId}")
    fun getAllMenu(
        @Path("userId") userId: String
    ): Call<List<MenuResponse>>

    /**
     * トレーニングメニュー追加API
     */
    @FormUrlEncoded
    @POST("menu/add")
    fun addMenu(
        @Field("musclepartid") musclePartId: String,
        @Field("menuname") menuName: String
    ): Call<List<MenuResponse>>

    /**
     * トレーニング部位取得API
     */
    @GET("musclepart")
    fun getAllMusclePart(): Call<List<MusclePartResponse>>

    @GET("log/{userId}")
    fun getAllLog(@Path("userId") userId: String): Call<List<LogResponse>>

    @FormUrlEncoded
    @POST("log/add")
    fun addLog(
        @Field("menuId") menuId: String,
        @Field("menuName") menuName: String,
        @Field("trainingWeight") trainingWeight: String,
        @Field("trainingCount") trainingCount: String,
        @Field("trainingDate") trainingDate: String,
        @Field("userId") userId: String
    ): Call<Boolean>
}
