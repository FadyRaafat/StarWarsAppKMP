package com.fady.starwars.domain.usecase

import com.fady.starwars.domain.model.Planet
import com.fady.starwars.domain.repository.IStarWarsRepository

class GetPlanetUseCase(
    private val repository: IStarWarsRepository
) {
    suspend operator fun invoke(planetId: String): Result<Planet> {
        if (planetId.isBlank()) {
            return Result.failure(IllegalArgumentException("Planet ID cannot be empty"))
        }
        
        return repository.getPlanet(planetId.trim())
    }
}