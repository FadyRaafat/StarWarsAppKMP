package com.fady.starwars.domain.usecase

import com.fady.starwars.domain.model.Person
import com.fady.starwars.domain.repository.IStarWarsRepository

class SearchPeopleUseCase(
    private val repository: IStarWarsRepository
) {
    suspend operator fun invoke(query: String): Result<List<Person>> {
        if (query.isBlank()) {
            return Result.failure(IllegalArgumentException("Search query cannot be empty"))
        }
        
        return repository.searchPeople(query.trim())
    }
}