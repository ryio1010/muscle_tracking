package com.example.muscletracking.repository.user

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.model.user.User
import com.example.muscletracking.model.user.UserResponse

class UserRepository(app: Application) {

    // TODO:　レスポンスコードによる処理分岐

    private val userDao = AppDatabase.getInstance(app).userDao()
    private val retrofitClient = RetrofitClient.getApiService()

    /**
     * IdによるローカルDB検索
     */
    @WorkerThread
    suspend fun getUserById(userId: String): User {
        return userDao.getUserById(userId)
    }

    /**
     * ローカルDBに登録処理
     */
    @WorkerThread
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    /**
     * loginAPI
     */
    @WorkerThread
    suspend fun login(userid: String, password: String): UserResponse? {
        val response = retrofitClient.login(userid, password).execute()
        return response.body()
    }

    /**
     * registerAPI
     */
    @WorkerThread
    suspend fun register(userid: String, username: String, password: String): Boolean? {
        val response = retrofitClient.register(userid, username, password).execute()
        return if (response.isSuccessful) {
            Log.d("debug", response.code().toString())
            response.body()
        } else {
            Log.d("debug", response.code().toString())
            false
        }
    }

    /**
     * updateAPI
     */
    @WorkerThread
    suspend fun updateUserInfo(
        userid: String,
        username: String,
        password: String
    ): UserResponse? {
        val response =
            retrofitClient.updateUserInfo(userid, username, password).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}