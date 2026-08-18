package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Strict Black, White, and Red Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = RubricRedLight,
    onPrimary = PureWhite,
    primaryContainer = RubricRedDark,
    onPrimaryContainer = PureWhite,
    secondary = PureWhite,
    onSecondary = PureBlack,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = PureWhite,
    tertiary = RubricRedLight,
    onTertiary = PureWhite,
    background = NightBlack,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMutedLight,
    outline = DarkBorder,
    outlineVariant = DarkSurfaceVariant
)

// Strict Black, White, and Red Light Theme
private val LightColorScheme = lightColorScheme(
    primary = RubricRed,
    onPrimary = PureWhite,
    primaryContainer = RubricRed,
    onPrimaryContainer = PureWhite,
    secondary = PureBlack,
    onSecondary = PureWhite,
    secondaryContainer = WhiteSurfaceVariant,
    onSecondaryContainer = PureBlack,
    tertiary = RubricRed,
    onTertiary = PureWhite,
    background = OffWhite,
    onBackground = TextBlack,
    surface = WhiteSurface,
    onSurface = TextBlack,
    surfaceVariant = WhiteSurfaceVariant,
    onSurfaceVariant = TextMutedDark,
    outline = WhiteBorder,
    outlineVariant = WhiteSurfaceVariant
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
