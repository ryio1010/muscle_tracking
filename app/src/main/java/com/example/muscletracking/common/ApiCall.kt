package com.example.muscletracking.common

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import retrofit2.Response

inline fun <reified T : Any> apiCall(crossinline call: suspend () -> Response<T>): Flow<ApiResult<T>> =
    flow<ApiResult<T>> {
        val response = call()
        if (response.isSuccessful) {emit(ApiResult.Success(response.body()!!))
        Log.d("debug",response.body().toString())}
        else throw HttpException(response)
    }.catch { it: Throwable ->
        emit(ApiResult.Error(it))
        Log.d("debug",it.toString())
    }.onStart {
        emit(ApiResult.Proceeding)
    }.flowOn(Dispatchers.IO)