package com.example.muscletracking.repository.user

import androidx.annotation.WorkerThread
import com.example.muscletracking.dao.UserDao
import com.example.muscletracking.model.user.User

class UserRepository(private val userDao: UserDao) {

    @WorkerThread
    suspend fun getUserAll(): List<User> {
        return userDao.getAllUser()
    }

    @WorkerThread
    suspend fun getUserByName(userName:String):User {
        return userDao.getUserById(userName)
    }

    @WorkerThread
    suspend fun insertUser(user:User) {
        userDao.insertUser(user)
    }

}