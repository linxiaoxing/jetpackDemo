package com.example.navigationcomponentstutorials.api

sealed class DataResult<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {

    class Loading<T> : DataResult<T>(Status.LOADING)
    class Success<T>(data: T) : DataResult<T>(Status.SUCCESS, data = data)
    class Error<T>(error: Throwable) : DataResult<T>(Status.ERROR, error = error)

}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}