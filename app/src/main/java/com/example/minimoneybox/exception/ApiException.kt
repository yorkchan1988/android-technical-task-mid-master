package com.example.minimoneybox.exception

// Exception used to handle errors returned from server
class ApiException constructor(name: String, message: String) : RuntimeException(message) {
    val name: String = name
    override val message: String = message
}