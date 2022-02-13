package com.example.muscletracking.repository.user

import android.app.Application
import androidx.annotation.WorkerThread
import com.example.muscletracking.config.db.AppDatabase
import com.example.muscletracking.config.api.RetrofitClient
import com.example.muscletracking.model.user.User

class UserRepository(app: Application) {

    private val userDao = AppDatabase.getInstance(app).userDao()
    private val retrofitClient = RetrofitClient.getApiService()

    @WorkerThread
    suspend fun getUserAll(): List<User> {
        return userDao.getAllUser()
    }

    @WorkerThread
    suspend fun getUserByName(userName: String): User {
        return userDao.getUserById(userName)
    }

    @WorkerThread
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    @WorkerThread
    suspend fun getUserInfo(userName: String): com.example.muscletracking.config.api.User {
        val user = retrofitClient.getUserByName(userName).execute().body()
        return user!!
    }


}