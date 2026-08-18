package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LiturgicalGoldLight,
    onPrimary = LiturgicalCrimsonDark,
    primaryContainer = LiturgicalCrimsonDark,
    onPrimaryContainer = LiturgicalGoldLight,
    secondary = LiturgicalGold,
    onSecondary = NightBackground,
    secondaryContainer = NightSurfaceVariant,
    onSecondaryContainer = LiturgicalGoldLight,
    tertiary = RubricRedLight,
    onTertiary = Color.White,
    background = NightBackground,
    onBackground = NightOnSurface,
    surface = NightSurface,
    onSurface = NightOnSurface,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = NightOnSurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = LiturgicalCrimson,
    onPrimary = Color.White,
    primaryContainer = LiturgicalCrimsonDark,
    onPrimaryContainer = LiturgicalGoldLight,
    secondary = LiturgicalGoldDark,
    onSecondary = Color.White,
    secondaryContainer = ParchmentSurfaceVariant,
    onSecondaryContainer = LiturgicalCrimsonDark,
    tertiary = RubricRed,
    onTertiary = Color.White,
    background = ParchmentBackground,
    onBackground = ParchmentOnSurface,
    surface = ParchmentSurface,
    onSurface = ParchmentOnSurface,
    surfaceVariant = ParchmentSurfaceVariant,
    onSurfaceVariant = ParchmentOnSurfaceVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
