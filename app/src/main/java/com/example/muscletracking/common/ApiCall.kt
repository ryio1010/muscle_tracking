package com.example.muscletracking.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import retrofit2.Response

inline fun <reified T : Any> ApiCall(crossinline call: suspend () -> Response<T>): Flow<ApiResult<T>> =
    flow<ApiResult<T>> {
        val response = call()
        if (response.isSuccessful) emit(ApiResult.Success(response.body()!!))
        else throw HttpException(response)
    }.catch { it: Throwable ->
        emit(ApiResult.Error(it))
    }.onStart {
        emit(ApiResult.Proceeding)
    }.flowOn(Dispatchers.IO)