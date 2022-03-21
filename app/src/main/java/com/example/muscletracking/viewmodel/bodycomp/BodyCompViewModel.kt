package com.example.muscletracking.viewmodel.bodycomp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muscletracking.model.bodycomp.BodyComp
import com.example.muscletracking.model.bodycomp.BodyCompResponse
import com.example.muscletracking.repository.bodycomp.BodyCompRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BodyCompViewModel(app: Application) : AndroidViewModel(app) {
    private val repository: BodyCompRepository = BodyCompRepository(app)

    val latestBodyComp: MutableLiveData<BodyComp> = MutableLiveData()
    val bodyCompList: MutableLiveData<List<BodyCompResponse>> = MutableLiveData()
    val insertedBodyComp: MutableLiveData<BodyCompResponse> = MutableLiveData()
    val updatedBodyComp: MutableLiveData<BodyCompResponse> = MutableLiveData()

    // coroutine用
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getLatestBodyCompOfDb() = scope.launch(Dispatchers.IO) {
        val bodyComp = repository.getLatestBodyComp()
        latestBodyComp.postValue(bodyComp)
    }

    fun insertBodyCompDb(bodyComp: BodyComp) = scope.launch(Dispatchers.IO) {
        repository.insertBodyCompDb(bodyComp)
    }

    fun updateBodyCompDb(bodyComp: BodyComp) = scope.launch(Dispatchers.IO) {
        repository.updateBodyCompDb(bodyComp)
    }

    fun getAllBodyComp(userId: String) = scope.launch(Dispatchers.IO) {
        val allBodyComp = repository.getAllBodyComp(userId)
        bodyCompList.postValue(allBodyComp)
    }

    fun insertBodyComp(height: Double, weight: Double, bfp: Double, bodyCompDate: String,userId: String) =
        scope.launch(Dispatchers.IO) {
            val bodyComp = repository.insertBodyComp(height, weight, bfp, bodyCompDate,userId)
            insertedBodyComp.postValue(bodyComp)
        }

    fun updateBodyComp(bodyCompId: Int, height: Double, weight: Double, bfp: Double,userId: String) =
        scope.launch(Dispatchers.IO) {
            val bodyComp = repository.updateBodyComp(bodyCompId, height, weight, bfp,userId)
            updatedBodyComp.postValue(bodyComp)
        }
}