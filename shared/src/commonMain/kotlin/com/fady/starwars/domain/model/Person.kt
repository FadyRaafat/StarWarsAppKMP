package com.fady.starwars.domain.model

data class Person(
    val name: String,
    val homeworldUrl: String
) {
    val planetId: String
        get() {
            return try {
                if (homeworldUrl.isBlank()) return "1"
                
                val cleanUrl = homeworldUrl.trim().removeSuffix("/")
                val segments = cleanUrl.split("/")
                val id = segments.lastOrNull { it.isNotBlank() && it.all { char -> char.isDigit() } }
                
                // Validate that the ID is a positive integer
                if (id != null && id.toIntOrNull() != null && id.toInt() > 0) {
                    id
                } else {
                    "1" // Default to Tatooine if parsing fails
                }
            } catch (e: Exception) {
                "1" // Default to Tatooine if any error occurs
            }
        }
    
    val displayName: String
        get() = name.takeIf { it.isNotBlank() } ?: "Unknown Character"
}