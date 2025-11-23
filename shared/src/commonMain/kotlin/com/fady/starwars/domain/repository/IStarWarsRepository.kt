package com.fady.starwars.domain.repository

import com.fady.starwars.domain.model.Person
import com.fady.starwars.domain.model.Planet

interface IStarWarsRepository {
    suspend fun searchPeople(query: String): Result<List<Person>>
    suspend fun getPlanet(id: String): Result<Planet>
}