package com.example.muscletracking.config.api

import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.model.bodycomp.BodyCompResponse
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
    ): Call<MenuResponse>

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
        @Field("trainingMemo") trainingMemo: String,
        @Field("userId") userId: String
    ): Call<LogResponse>

    /**
     * ログ履歴更新API
     */
    @FormUrlEncoded
    @PUT("log")
    fun updateLog(
        @Field("logId") logId: String,
        @Field("menuId") menuId: String,
        @Field("menuName") menuName: String,
        @Field("trainingWeight") trainingWeight: String,
        @Field("trainingCount") trainingCount: String,
        @Field("trainingDate") trainingDate: String,
        @Field("trainingMemo") trainingMemo: String,
        @Field("userId") userId: String
    ): Call<LogResponse>

    /**
     * ログ履歴削除API
     */
    @DELETE("log/delete/{logId}")
    fun deleteLog(@Path("logId") logId: String): Call<String>


    /**
     * 体組成データ取得API
     */
    @GET("bodycomp/{userId}")
    fun getAllBodyComp(@Path("userId") userId: String): Call<List<BodyCompResponse>>

    /**
     * 体組成データ追加API
     */
    @FormUrlEncoded
    @POST("bodycomp/add")
    fun addBodyComp(
        @Field("height") height: String,
        @Field("weight") weight: String,
        @Field("bfp") bfp: String,
        @Field("bodyCompDate") bodyCompDate: String,
        @Field("userId") userId: String
    ): Call<BodyCompResponse>

    /**
     * 体組成データ更新API
     */
    @FormUrlEncoded
    @PUT("bodycomp/{bodyCompId}")
    fun updateBodyComp(
        @Path("bodyCompId") bodyCompId: Int,
        @Field("height") height: String,
        @Field("weight") weight: String,
        @Field("bfp") bfp: String,
        @Field("userId") userId: String
    ): Call<BodyCompResponse>
}
