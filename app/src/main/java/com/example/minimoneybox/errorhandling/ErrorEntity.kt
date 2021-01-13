package com.example.minimoneybox.errorhandling

import com.example.minimoneybox.models.response.ErrorResponse

sealed class ErrorEntity {
    data class ApiError(val errorResponse: ErrorResponse) : ErrorEntity()
    data class ValidationError(val errorResponse: ErrorResponse) : ErrorEntity()
    data class UnexpectedError(val message: String) : ErrorEntity()
}