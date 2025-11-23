package com.fady.starwars.data.remote.dto

import kotlinx.serialization.Serializable
import com.fady.starwars.domain.model.Person

@Serializable
data class PersonDto(
    val name: String? = null,
    val homeworld: String? = null
) {
    fun toDomain(): Person {
        return Person(
            name = name?.trim() ?: "Unknown Character",
            homeworldUrl = homeworld?.trim() ?: ""
        )
    }
}