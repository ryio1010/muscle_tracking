package com.example.muscletracking.dao.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.muscletracking.model.user.User

@Dao
interface UserDao {

    /**
     * Idによるユーザー検索
     */
    @Query("SELECT * FROM user WHERE userid = :userId")
    fun getUserById(userId:String) : User

    /**
     * ユーザー登録
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    /**
     * ユーザー情報更新
     */
    @Update
    fun updateUser(user:User)

    /**
     * ユーザー削除
     */
    @Delete
    fun deleteUser(user:User)
}