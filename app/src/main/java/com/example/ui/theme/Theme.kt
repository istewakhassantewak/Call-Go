package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF63B3ED),
    onPrimary = Navy900,
    primaryContainer = Navy800,
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = OrangePrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF5E2B00),
    onSecondaryContainer = OrangeLight,
    tertiary = TealAccent,
    onTertiary = Navy900,
    tertiaryContainer = TealDark,
    onTertiaryContainer = TealLight,
    background = Color(0xFF0A121E),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF101C2E),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E2F47),
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A)
)

private val LightColorScheme = lightColorScheme(
    primary = Navy800,
    onPrimary = Color.White,
    primaryContainer = Navy100,
    onPrimaryContainer = Navy900,
    secondary = OrangePrimary,
    onSecondary = Color.White,
    secondaryContainer = OrangeLight,
    onSecondaryContainer = OrangeDark,
    tertiary = TealPrimary,
    onTertiary = Color.White,
    tertiaryContainer = TealLight,
    onTertiaryContainer = TealDark,
    background = Slate50,
    onBackground = Slate900,
    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun CallAndGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep branded palette intact
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) = CallAndGoTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)

