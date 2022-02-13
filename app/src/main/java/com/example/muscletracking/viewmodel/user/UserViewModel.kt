package com.example.muscletracking.viewmodel.user

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muscletracking.AppDatabase
import com.example.muscletracking.model.user.User
import com.example.muscletracking.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserViewModel(app: Application) : AndroidViewModel(app) {
    lateinit var repository: UserRepository
    lateinit var user : User

    var userList : MutableLiveData<List<User>> = MutableLiveData<List<User>>()

    init {
        val userDao = AppDatabase.getInstance(app).userDao()
        repository = UserRepository(userDao)
    }

    // coroutine用
    private var parentJob = Job()
    private val coroutineContext : CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insertUser(user:User) = scope.launch(Dispatchers.IO) {
        repository.insertUser(user)
    }

    fun selectAllUsers() = scope.launch(Dispatchers.IO) {
        val users = repository.getUserAll()
        Log.d("debug",users.toString())
        userList.postValue(users)
    }

    fun selectUserByName(userName:String) = scope.launch(Dispatchers.IO) {
        user = repository.getUserByName(userName)
    }
}