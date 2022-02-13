package com.example.muscletracking.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.muscletracking.model.user.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM user WHERE userid = :userName")
    fun getUserById(userName:String) : User

    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user:User)

    @Delete
    fun deleteUser(user:User)
}