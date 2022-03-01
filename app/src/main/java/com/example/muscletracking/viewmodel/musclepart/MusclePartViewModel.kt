package com.example.muscletracking.viewmodel.musclepart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.muscletracking.model.musclepart.MusclePart
import com.example.muscletracking.model.musclepart.MusclePartResponse
import com.example.muscletracking.repository.musclepart.MusclePartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MusclePartViewModel(app:Application): AndroidViewModel(app) {
    private val musclePartRepository: MusclePartRepository = MusclePartRepository(app)

    val musclePartList : MutableLiveData<List<MusclePartResponse>> = MutableLiveData()
    val musclePartListOfDB : MutableLiveData<List<MusclePart>> = MutableLiveData()

    // coroutine用
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun getAllMusclePartFromDB() = scope.launch(Dispatchers.IO) {
        val allMusclePart = musclePartRepository.getAllMusclePartFromDB()
        musclePartListOfDB.postValue(allMusclePart)
    }

    fun insertMusclePart(musclePart: MusclePart) = scope.launch(Dispatchers.IO) {
        musclePartRepository.insertMusclePart(musclePart)
    }

    fun getAllMusclePart() = scope.launch(Dispatchers.IO) {
        val allMusclePart = musclePartRepository.getAllMusclePart()
        musclePartList.postValue(allMusclePart)
    }
}