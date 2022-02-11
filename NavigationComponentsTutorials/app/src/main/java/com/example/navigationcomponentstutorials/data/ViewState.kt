package com.example.navigationcomponentstutorials.data

import com.example.navigationcomponentstutorials.api.Status


class ViewState<T>(
    val status: Status,
    val data: T? = null,
    val error: Throwable? = null
) {
    fun isLoading() = status == Status.LOADING

    fun getErrorMessage() = error?.message

    fun shouldShowErrorMessage() = error != null
}