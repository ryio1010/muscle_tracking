package com.example.muscletracking.viewmodel.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.muscletracking.repository.log.LogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LogViewModel(app:Application):AndroidViewModel(app) {
    private val repository:LogRepository = LogRepository(app)


    // coroutine用
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

}