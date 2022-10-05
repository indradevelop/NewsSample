package com.example.news.api

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T? = null) : NetworkResult<T>(data)
    class SuccessMessage<T>(data: T? = null, message: String?) : NetworkResult<T>(data,message)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
}