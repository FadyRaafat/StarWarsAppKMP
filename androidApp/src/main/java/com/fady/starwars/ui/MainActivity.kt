package com.fady.starwars.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.view.WindowCompat
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fady.starwars.BuildConfig
import com.fady.starwars.domain.model.Person
import com.fady.starwars.presentation.viewmodel.StarWarsViewModel
import com.fady.starwars.ui.screen.PlanetDetailModal
import com.fady.starwars.ui.screen.SearchScreen
import com.fady.starwars.ui.theme.AppFlavor
import com.fady.starwars.ui.theme.StarWarsTheme

class MainActivity : ComponentActivity() {
    private val viewModel = StarWarsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Hide system UI (status bar and action bar)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val appFlavor = when {
            BuildConfig.FLAVOR.contains("flavorA", ignoreCase = true) -> AppFlavor.FLAVOR_A
            BuildConfig.FLAVOR.contains("flavorB", ignoreCase = true) -> AppFlavor.FLAVOR_B
            else -> AppFlavor.FLAVOR_A
        }
        
        setContent {
            StarWarsTheme(flavor = appFlavor) {
                StarWarsApp(viewModel = viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }
}

@Composable
fun StarWarsApp(
    viewModel: StarWarsViewModel,
    modifier: Modifier = Modifier
) {
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val planetState by viewModel.planetState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    var showPlanetModal by rememberSaveable { mutableStateOf(false) }
    var selectedPerson by remember { mutableStateOf<Person?>(null) }

    SearchScreen(
        searchState = searchState,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSearchClick = viewModel::searchPeople,
        onPersonClick = { person ->
            selectedPerson = person
            showPlanetModal = true
            viewModel.loadPlanet(person.planetId)
        },
        modifier = modifier.fillMaxSize()
    )
    
    if (showPlanetModal && selectedPerson != null) {
        PlanetDetailModal(
            planetState = planetState,
            onDismiss = {
                showPlanetModal = false
                selectedPerson = null
                viewModel.clearPlanetState()
            },
            onRetry = {
                selectedPerson?.let { person ->
                    viewModel.retryPlanet(person.planetId)
                }
            }
        )
    }
}