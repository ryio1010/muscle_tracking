package com.example.muscletracking.viewmodel.user

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muscletracking.model.user.User
import com.example.muscletracking.model.user.UserResponse
import com.example.muscletracking.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: UserRepository = UserRepository(app)

    var selectUser: MutableLiveData<User> = MutableLiveData<User>()
    val mUserInfo: MutableLiveData<UserResponse> = MutableLiveData()
    val mUserInfoUpdate: MutableLiveData<UserResponse> = MutableLiveData()
    val isUserRegistered: MutableLiveData<Boolean> = MutableLiveData()

    // coroutine用
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    /**
     * ローカルDB　UserテーブルInsert
     */
    fun insertUser(user: User) = scope.launch(Dispatchers.IO) {
        repository.insertUser(user)
    }

    /**
     * ローカルDB　UserテーブルId検索
     */
    fun selectUserById(userId: String) = scope.launch(Dispatchers.IO) {
        val user = repository.getUserById(userId)
        selectUser.postValue(user)
    }

    /**
     * loginAPI実行
     */
    fun login(userid: String, password: String) = scope.launch(Dispatchers.IO) {
        val user = repository.login(userid, password)
        if (user == null) {
            mUserInfo.postValue(null)
        } else {
            mUserInfo.postValue(user)
        }
    }

    /**
     * registerAPI実行
     */
    fun register(userId: String, userName: String, password: String) =
        scope.launch(Dispatchers.IO) {
            val registerFlag = repository.register(userId, userName, password)
            isUserRegistered.postValue(registerFlag)
        }

    /**
     * updateAPI実行
     */
    fun updateUserInfo(
        userId: String,
        userName: String,
        password: String
    ) =
        scope.launch(Dispatchers.IO) {
            val user = repository.updateUserInfo(userId, userName, password)
            mUserInfoUpdate.postValue(user)
        }
}