package com.fady.starwars

import com.fady.starwars.presentation.viewmodel.StarWarsViewModel

/**
 * Factory function to create StarWarsViewModel with all default dependencies.
 * This simplifies KMP usage from iOS where default parameters aren't well supported.
 */
fun createStarWarsViewModel(): StarWarsViewModel {
    return StarWarsViewModel()
}

