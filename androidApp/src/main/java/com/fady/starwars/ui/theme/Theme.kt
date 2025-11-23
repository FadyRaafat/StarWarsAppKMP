package com.fady.starwars.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

// FlavorA - Blue Theme
private val BlueFlavorLightColorScheme = lightColorScheme(
    primary = BlueFlavorPrimary,
    secondary = BlueFlavorSecondary,
    background = BlueFlavorBackground,
    surface = BlueFlavorSurface,
    onPrimary = BlueFlavorOnPrimary,
    onSecondary = BlueFlavorOnSecondary,
    onBackground = BlueFlavorOnBackground,
    onSurface = BlueFlavorOnSurface,
    error = ErrorColor
)

private val BlueFlavorDarkColorScheme = darkColorScheme(
    primary = BlueFlavorPrimary,
    secondary = BlueFlavorSecondary,
    background = BlueFlavorOnBackground,
    surface = BlueFlavorOnSurface,
    onPrimary = BlueFlavorOnPrimary,
    onSecondary = BlueFlavorOnSecondary,
    onBackground = BlueFlavorBackground,
    onSurface = BlueFlavorSurface,
    error = ErrorColor
)

// FlavorB - Purple Theme
private val PurpleFlavorLightColorScheme = lightColorScheme(
    primary = PurpleFlavorPrimary,
    secondary = PurpleFlavorSecondary,
    background = PurpleFlavorBackground,
    surface = PurpleFlavorSurface,
    onPrimary = PurpleFlavorOnPrimary,
    onSecondary = PurpleFlavorOnSecondary,
    onBackground = PurpleFlavorOnBackground,
    onSurface = PurpleFlavorOnSurface,
    error = ErrorColor
)

private val PurpleFlavorDarkColorScheme = darkColorScheme(
    primary = PurpleFlavorPrimary,
    secondary = PurpleFlavorSecondary,
    background = PurpleFlavorOnBackground,
    surface = PurpleFlavorOnSurface,
    onPrimary = PurpleFlavorOnPrimary,
    onSecondary = PurpleFlavorOnSecondary,
    onBackground = PurpleFlavorBackground,
    onSurface = PurpleFlavorSurface,
    error = ErrorColor
)

enum class AppFlavor {
    FLAVOR_A,
    FLAVOR_B
}

val LocalAppFlavor = staticCompositionLocalOf { AppFlavor.FLAVOR_A }

@Composable
fun StarWarsTheme(
    flavor: AppFlavor = AppFlavor.FLAVOR_A,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (flavor) {
        AppFlavor.FLAVOR_A -> if (darkTheme) BlueFlavorDarkColorScheme else BlueFlavorLightColorScheme
        AppFlavor.FLAVOR_B -> if (darkTheme) PurpleFlavorDarkColorScheme else PurpleFlavorLightColorScheme
    }

    CompositionLocalProvider(
        LocalAppFlavor provides flavor
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}