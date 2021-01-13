package com.example.minimoneybox.errorhandling

interface ErrorHandler {
    fun getError(throwable: Throwable): ErrorEntity
    fun getError(message: String): ErrorEntity
}