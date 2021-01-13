package com.example.minimoneybox.network

import com.example.minimoneybox.errorhandling.ErrorEntity

sealed class ApiResult<T>(
) {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error<T>(val error: ErrorEntity) : ApiResult<T>()
    class Loading<T>() : ApiResult<T>()
}