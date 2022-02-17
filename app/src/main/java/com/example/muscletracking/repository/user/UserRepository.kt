package com.example.muscletracking.repository.user

import android.app.Application
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.model.user.User
import com.example.muscletracking.model.user.UserResponse

class UserRepository(app: Application) {

    private val userDao = AppDatabase.getInstance(app).userDao()
    private val retrofitClient = RetrofitClient.getApiService()

    @WorkerThread
    suspend fun getUserAll(): List<User> {
        return userDao.getAllUser()
    }

    @WorkerThread
    suspend fun getUserById(userId: String): User {
        return userDao.getUserById(userId)
    }

    @WorkerThread
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    @WorkerThread
    suspend fun login(userid: String, password: String): UserResponse? {
        val response = retrofitClient.login(userid, password).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}