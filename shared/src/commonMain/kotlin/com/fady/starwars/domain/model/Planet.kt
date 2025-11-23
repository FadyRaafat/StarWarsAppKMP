package com.fady.starwars.domain.model

data class Planet(
    val name: String,
    val terrain: String,
    val gravity: String,
    val population: String
) {
    val displayName: String
        get() = name.takeIf { it.isNotBlank() && it != "null" } ?: "Unknown Planet"
    
    val displayTerrain: String
        get() = terrain.takeIf { it.isNotBlank() && it != "null" && it != "unknown" } ?: "Unknown"
    
    val displayGravity: String
        get() = gravity.takeIf { it.isNotBlank() && it != "null" && it != "unknown" } ?: "Unknown"
    
    val displayPopulation: String
        get() = when {
            population.isBlank() || population == "null" || population == "unknown" -> "Unknown"
            population == "0" -> "Uninhabited"
            else -> {
                // Format large numbers with commas
                val cleanPop = population.filter { it.isDigit() }
                if (cleanPop.isNotEmpty() && cleanPop.toLongOrNull() != null) {
                    formatNumber(cleanPop.toLong())
                } else {
                    population.replaceFirstChar { 
                        if (it.isLowerCase()) it.titlecase() else it.toString() 
                    }
                }
            }
        }
    
    private fun formatNumber(number: Long): String {
        return when {
            number >= 1_000_000_000 -> {
                val billions = number / 1_000_000_000.0
                if (billions % 1.0 == 0.0) "${billions.toInt()}B" else "${(billions * 10).toLong() / 10.0}B"
            }
            number >= 1_000_000 -> {
                val millions = number / 1_000_000.0
                if (millions % 1.0 == 0.0) "${millions.toInt()}M" else "${(millions * 10).toLong() / 10.0}M"
            }
            number >= 1_000 -> {
                val thousands = number / 1_000.0
                if (thousands % 1.0 == 0.0) "${thousands.toInt()}K" else "${(thousands * 10).toLong() / 10.0}K"
            }
            else -> number.toString()
        }
    }
    
    companion object {
        fun unknown() = Planet(
            name = "Unknown",
            terrain = "Unknown",
            gravity = "Unknown", 
            population = "Unknown"
        )
    }
}