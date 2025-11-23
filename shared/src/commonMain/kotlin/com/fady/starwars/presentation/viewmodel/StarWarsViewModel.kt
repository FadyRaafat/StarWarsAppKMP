package com.fady.starwars.presentation.viewmodel

import com.fady.starwars.data.repository.StarWarsRepository
import com.fady.starwars.domain.model.ApiError
import com.fady.starwars.domain.model.Person
import com.fady.starwars.domain.model.Planet
import com.fady.starwars.domain.repository.IStarWarsRepository
import com.fady.starwars.domain.usecase.GetPlanetUseCase
import com.fady.starwars.domain.usecase.SearchPeopleUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StarWarsViewModel(
    private val searchPeopleUseCase: SearchPeopleUseCase,
    private val getPlanetUseCase: GetPlanetUseCase,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) {
    constructor(
        repository: IStarWarsRepository = StarWarsRepository()
    ) : this(
        SearchPeopleUseCase(repository),
        GetPlanetUseCase(repository)
    )

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    private val _searchState = MutableStateFlow<UiState<List<Person>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<Person>>> = _searchState.asStateFlow()

    private val _planetState = MutableStateFlow<UiState<Planet>>(UiState.Idle)
    val planetState: StateFlow<UiState<Planet>> = _planetState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null
    private var planetJob: Job? = null

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchPeople(query: String) {
        if (query.isBlank()) {
            _searchState.value = UiState.Error("Search query cannot be empty")
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch(ioDispatcher) {
            _searchState.value = UiState.Loading
            
            searchPeopleUseCase(query).fold(
                onSuccess = { people ->
                    _searchState.value = if (people.isEmpty()) {
                        UiState.Error("No characters found for '$query'")
                    } else {
                        UiState.Success(people)
                    }
                },
                onFailure = { error ->
                    val errorMessage = when (error) {
                        is ApiError -> error.message ?: "API error occurred"
                        is IllegalArgumentException -> error.message ?: "Invalid search query"
                        else -> error.message ?: "Failed to search characters"
                    }
                    _searchState.value = UiState.Error(
                        message = errorMessage,
                        throwable = error
                    )
                }
            )
        }
    }

    fun loadPlanet(planetId: String) {
        planetJob?.cancel()
        planetJob = viewModelScope.launch(ioDispatcher) {
            _planetState.value = UiState.Loading
            
            getPlanetUseCase(planetId).fold(
                onSuccess = { planet ->
                    _planetState.value = UiState.Success(planet)
                },
                onFailure = { error ->
                    val errorMessage = when (error) {
                        is ApiError -> error.message ?: "API error occurred"
                        is IllegalArgumentException -> error.message ?: "Invalid planet ID"
                        else -> error.message ?: "Failed to load planet information"
                    }
                    _planetState.value = UiState.Error(
                        message = errorMessage,
                        throwable = error
                    )
                }
            )
        }
    }

    fun clearPlanetState() {
        _planetState.value = UiState.Idle
    }

    fun clearSearchState() {
        _searchState.value = UiState.Idle
    }

    fun retrySearch() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isNotBlank()) {
            searchPeople(currentQuery)
        }
    }

    fun retryPlanet(planetId: String) {
        loadPlanet(planetId)
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}