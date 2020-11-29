package com.example.minimoneybox.exception

class ApiException constructor(name: String, message: String) : RuntimeException(message) {
    val name: String = name
    override val message: String = message
}