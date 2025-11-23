package com.fady.starwars.domain.model

sealed class ApiError : Exception() {
    object NetworkError : ApiError() {
        override val message: String = "No internet connection. Please check your network and try again."
    }
    
    object TimeoutError : ApiError() {
        override val message: String = "Request timed out. Please try again."
    }
    
    data class ServerError(val code: Int) : ApiError() {
        override val message: String = when (code) {
            404 -> "Resource not found"
            500 -> "Server error. Please try again later."
            503 -> "Service temporarily unavailable"
            else -> "Server error ($code)"
        }
    }
    
    data class ParseError(override val message: String) : ApiError()
    
    data class UnknownError(override val message: String) : ApiError()
    
    companion object {
        fun fromException(exception: Throwable): ApiError {
            return when {
                exception.message?.contains("timeout", ignoreCase = true) == true -> TimeoutError
                exception.message?.contains("network", ignoreCase = true) == true -> NetworkError
                exception.message?.contains("connection", ignoreCase = true) == true -> NetworkError
                else -> UnknownError(exception.message ?: "An unknown error occurred")
            }
        }
    }
}