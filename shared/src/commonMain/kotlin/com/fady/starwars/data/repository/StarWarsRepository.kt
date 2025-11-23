package com.fady.starwars.data.repository

import com.fady.starwars.data.remote.StarWarsApi
import com.fady.starwars.data.remote.StarWarsApiException
import com.fady.starwars.data.remote.StarWarsApiImpl
import com.fady.starwars.domain.model.Person
import com.fady.starwars.domain.model.Planet
import com.fady.starwars.domain.repository.IStarWarsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class StarWarsRepository(
    private val api: StarWarsApi = StarWarsApiImpl(),
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : IStarWarsRepository {

    override suspend fun searchPeople(query: String): Result<List<Person>> = withContext(ioDispatcher) {
        return@withContext try {
            if (query.isBlank()) {
                Result.failure(IllegalArgumentException("Search query cannot be empty"))
            } else {
                val peopleDto = api.searchPeople(query.trim())
                val people = peopleDto.map { it.toDomain() }
                Result.success(people)
            }
        } catch (e: StarWarsApiException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(StarWarsApiException("Unexpected error occurred: ${e.message}", e))
        }
    }

    override suspend fun getPlanet(id: String): Result<Planet> = withContext(ioDispatcher) {
        return@withContext try {
            if (id.isBlank()) {
                Result.failure(IllegalArgumentException("Planet ID cannot be empty"))
            } else {
                val planetDto = api.getPlanet(id.trim())
                val planet = planetDto.toDomain()
                Result.success(planet)
            }
        } catch (e: StarWarsApiException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(StarWarsApiException("Unexpected error occurred: ${e.message}", e))
        }
    }
}