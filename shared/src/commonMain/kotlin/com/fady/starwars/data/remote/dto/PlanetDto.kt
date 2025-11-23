package com.fady.starwars.data.remote.dto

import kotlinx.serialization.Serializable
import com.fady.starwars.domain.model.Planet

@Serializable
data class PlanetDto(
    val name: String? = null,
    val terrain: String? = null,
    val gravity: String? = null,
    val population: String? = null
) {
    fun toDomain(): Planet {
        return Planet(
            name = name?.trim() ?: "Unknown",
            terrain = terrain?.trim() ?: "Unknown",
            gravity = gravity?.trim() ?: "Unknown",
            population = population?.trim() ?: "Unknown"
        )
    }
}