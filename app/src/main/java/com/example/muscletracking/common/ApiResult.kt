package com.example.muscletracking.common

import java.lang.Exception

sealed class ApiResult<out T> {
    object Proceeding : ApiResult<Nothing>()
    data class Success<out T : Any>(val value: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
}
