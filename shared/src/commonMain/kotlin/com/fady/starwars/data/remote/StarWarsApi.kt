package com.fady.starwars.data.remote

import com.fady.starwars.data.remote.dto.PeopleResponse
import com.fady.starwars.data.remote.dto.PersonDto
import com.fady.starwars.data.remote.dto.PlanetDto
import com.fady.starwars.domain.model.ApiError
import com.fady.starwars.platform.createConfiguredHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlin.coroutines.CoroutineContext

interface StarWarsApi {
    suspend fun searchPeople(query: String): List<PersonDto>
    suspend fun getPlanet(id: String): PlanetDto
}

class StarWarsApiImpl(
    private val httpClient: HttpClient = createConfiguredHttpClient(),
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : StarWarsApi {

    companion object {
        private const val BASE_URL = "https://swapi.dev/api/"
    }

    override suspend fun searchPeople(query: String): List<PersonDto> = withContext(ioDispatcher) {
        if (query.isBlank()) {
            throw IllegalArgumentException("Search query cannot be empty")
        }
        
        val allPeople = mutableListOf<PersonDto>()
        var nextUrl: String? = "${BASE_URL}people/?search=${query.trim()}"
        var pageCount = 0
        val maxPages = 10 // Safety limit to prevent infinite loops

        while (nextUrl != null && pageCount < maxPages) {
            try {
                val response = httpClient.get {
                    url(nextUrl)
                }
                val peopleResponse: PeopleResponse = response.body()
                allPeople.addAll(peopleResponse.results)
                nextUrl = peopleResponse.next
                pageCount++
            } catch (e: HttpRequestTimeoutException) {
                throw ApiError.TimeoutError
            } catch (e: ClientRequestException) {
                throw ApiError.ServerError(e.response.status.value)
            } catch (e: ServerResponseException) {
                throw ApiError.ServerError(e.response.status.value)
            } catch (e: SerializationException) {
                throw ApiError.ParseError("Failed to parse response: ${e.message}")
            } catch (e: Exception) {
                throw ApiError.fromException(e)
            }
        }

        return@withContext allPeople
    }

    override suspend fun getPlanet(id: String): PlanetDto = withContext(ioDispatcher) {
        if (id.isBlank()) {
            throw IllegalArgumentException("Planet ID cannot be empty")
        }
        
        try {
            val response = httpClient.get {
                url("${BASE_URL}planets/${id.trim()}/")
            }
            return@withContext response.body<PlanetDto>()
        } catch (e: HttpRequestTimeoutException) {
            throw ApiError.TimeoutError
        } catch (e: ClientRequestException) {
            throw ApiError.ServerError(e.response.status.value)
        } catch (e: ServerResponseException) {
            throw ApiError.ServerError(e.response.status.value)
        } catch (e: SerializationException) {
            throw ApiError.ParseError("Failed to parse planet data: ${e.message}")
        } catch (e: Exception) {
            throw ApiError.fromException(e)
        }
    }
}

