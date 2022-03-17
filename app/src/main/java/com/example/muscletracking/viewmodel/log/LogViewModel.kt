package com.example.muscletracking.viewmodel.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muscletracking.model.log.Log
import com.example.muscletracking.model.log.LogResponse
import com.example.muscletracking.repository.log.LogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LogViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: LogRepository = LogRepository(app)

    val logList: MutableLiveData<List<LogResponse>> = MutableLiveData()
    val logListOfDB: MutableLiveData<List<Log>> = MutableLiveData()
    val logListByMenu: MutableLiveData<List<Log>> = MutableLiveData()
    val isLogAdded: MutableLiveData<Boolean> = MutableLiveData()


    // coroutine用
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getAllLogFromDB() = scope.launch(Dispatchers.IO) {
        val allLog = repository.getAllLogFromDB()
        logListOfDB.postValue(allLog)
    }

    fun getLogByMenu(menuName: String) = scope.launch(Dispatchers.IO) {
            val allLog = repository.getLogByMenu(menuName)
            logListByMenu.postValue(allLog)
    }

    fun insertLogOfDB(log: Log) = scope.launch(Dispatchers.IO) {
        repository.insertLogOfDB(log)
    }

    fun getAllLog(userId: String) = scope.launch(Dispatchers.IO) {
        val allLog = repository.getAllLog(userId)
        logList.postValue(allLog)
    }

    fun addLog(
        menuId: String,
        menuName: String,
        trainingWeight: String,
        trainingCount: String,
        trainingDate: String,
        userId: String
    ) = scope.launch(Dispatchers.IO) {
        repository.insertLog(menuId, menuName, trainingWeight, trainingCount, trainingDate, userId)
        isLogAdded.postValue(true)
    }
}