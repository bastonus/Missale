package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Strict Black, White, and Red Dark Theme with Unified Background
private val DarkColorScheme = darkColorScheme(
    primary = RubricRedLight,
    onPrimary = PureWhite,
    primaryContainer = RubricRedDark,
    onPrimaryContainer = PureWhite,
    secondary = PureWhite,
    onSecondary = PureBlack,
    secondaryContainer = NightBlack,
    onSecondaryContainer = PureWhite,
    tertiary = RubricRedLight,
    onTertiary = PureWhite,
    background = NightBlack,
    onBackground = TextWhite,
    surface = NightBlack,
    onSurface = TextWhite,
    surfaceVariant = NightBlack,
    onSurfaceVariant = TextMutedLight,
    outline = DarkBorder,
    outlineVariant = DarkBorder
)

// Strict Black, White, and Red Light Theme with Unified Background
private val LightColorScheme = lightColorScheme(
    primary = RubricRed,
    onPrimary = PureWhite,
    primaryContainer = RubricRed,
    onPrimaryContainer = PureWhite,
    secondary = PureBlack,
    onSecondary = PureWhite,
    secondaryContainer = PureWhite,
    onSecondaryContainer = PureBlack,
    tertiary = RubricRed,
    onTertiary = PureWhite,
    background = PureWhite,
    onBackground = TextBlack,
    surface = PureWhite,
    onSurface = TextBlack,
    surfaceVariant = PureWhite,
    onSurfaceVariant = TextMutedDark,
    outline = WhiteBorder,
    outlineVariant = WhiteBorder
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
