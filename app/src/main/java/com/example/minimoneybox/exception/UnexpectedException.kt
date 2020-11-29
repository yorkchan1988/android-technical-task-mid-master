package com.example.minimoneybox.exception

import java.lang.RuntimeException

// Exception used to handle all unexpected exceptions
class UnexpectedException(message: String): RuntimeException(message)