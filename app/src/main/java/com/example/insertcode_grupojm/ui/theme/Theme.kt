package com.example.insertcode_grupojm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta Modo Oscuro: EVA-01 (Neon Genesis Evangelion)
private val Eva01DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9D4EDD),              // Púrpura principal EVA-01
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF3C096C),     // Púrpura oscuro para contenedores/chips
    onPrimaryContainer = Color(0xFFE0AAFF),

    secondary = Color(0xFF52FF00),            // Verde neón EVA-01
    onSecondary = Color(0xFF0A1F00),
    secondaryContainer = Color(0xFF1B3B14),   // Verde oscuro táctico
    onSecondaryContainer = Color(0xFFA8FF78),

    tertiary = Color(0xFFFF7B00),             // Naranja / Ámbar NERV MAGI
    onTertiary = Color(0xFF1F0D00),
    tertiaryContainer = Color(0xFF4A2000),
    onTertiaryContainer = Color(0xFFFFB703),

    background = Color(0xFF0D0B14),           // Negro violáceo profundo
    onBackground = Color(0xFFF3EFFA),
    surface = Color(0xFF0D0B14),
    onSurface = Color(0xFFF3EFFA),

    surfaceVariant = Color(0xFF1C172B),       // Tarjetas y superficies elevadas
    onSurfaceVariant = Color(0xFFC7BFE0),
    surfaceContainer = Color(0xFF181426),
    surfaceContainerLow = Color(0xFF141020),
    surfaceContainerHigh = Color(0xFF231D38),

    outline = Color(0xFF5A4E7C),
    outlineVariant = Color(0xFF2E2647),
    error = Color(0xFFFF3366),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF590D22),
    onErrorContainer = Color(0xFFFFCCD5)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    secondary = Color(0xFF10B981),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD1FAE5),
    onSecondaryContainer = Color(0xFF065F46),
    tertiary = Color(0xFFF59E0B),
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFFE2E8F0)
)

@Composable
fun InsertCode_GrupoJMTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) Eva01DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}