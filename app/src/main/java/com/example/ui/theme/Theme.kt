package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF1E1500),
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = CyanAccent,
    onSecondary = Color(0xFF003548),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = IndigoAccent,
    onTertiary = Color.White,
    background = QuizDarkBg,
    onBackground = TextLightPrimary,
    surface = QuizSurface,
    onSurface = TextLightPrimary,
    surfaceVariant = QuizSurfaceVariant,
    onSurfaceVariant = TextLightSecondary,
    surfaceContainer = QuizSurfaceContainer,
    outline = QuizBorder,
    error = CrimsonStrike,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = GoldSecondary,
    onPrimary = Color.White,
    primaryContainer = GoldLight,
    onPrimaryContainer = GoldDark,
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = IndigoAccent,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    surfaceContainer = Color(0xFFF1F5F9),
    outline = Color(0xFFCBD5E1),
    error = CrimsonStrike,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // QuizEarn defaults to its signature dark gaming theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
