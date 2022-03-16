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
        @Field("userId") userId: String,
        @Field("userName") userName: String,
        @Field("password") password: String
    ): Call<Boolean>

    /**
     * ユーザーログインAPI
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("userId") userid: String,
        @Field("password") password: String
    ): Call<UserResponse>

    /**
     * ユーザー情報更新API
     */
    @FormUrlEncoded
    @PUT("user")
    fun updateUserInfo(
        @Field("userId") userid: String,
        @Field("userName") username: String,
        @Field("password") password: String,
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
        @Field("musclePartId") musclePartId: String,
        @Field("menuName") menuName: String,
        @Field("userId") userId: String
    ): Call<List<MenuResponse>>

    /**
     * トレーニング部位取得API
     */
    @GET("musclepart")
    fun getAllMusclePart(): Call<List<MusclePartResponse>>

    /**
     * ログ履歴取得API
     */
    @GET("log/{userId}")
    fun getAllLog(@Path("userId") userId: String): Call<List<LogResponse>>

    /**
     * ログ履歴追加API
     */
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
