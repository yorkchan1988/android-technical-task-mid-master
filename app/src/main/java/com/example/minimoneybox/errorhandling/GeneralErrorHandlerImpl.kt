package com.example.minimoneybox.errorhandling

import com.example.minimoneybox.models.response.ErrorResponse
import retrofit2.HttpException
import javax.inject.Inject

class GeneralErrorHandlerImpl @Inject constructor(): ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when(throwable) {
            is HttpException -> {
                val errorResponse = ErrorResponse.fromHttpException(throwable)
                ErrorEntity.ApiError(errorResponse)
            }
            else -> ErrorEntity.UnexpectedError(throwable.message ?: "")
        }
    }

    override fun getError(message: String): ErrorEntity {
        return ErrorEntity.UnexpectedError(message)
    }
}