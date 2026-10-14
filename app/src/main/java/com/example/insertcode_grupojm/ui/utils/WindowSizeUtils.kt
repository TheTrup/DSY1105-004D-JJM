package com.example.insertcode_grupojm.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

// Clases de tamaño de ventana (mismos cortes que las Window Size Classes de Material 3):
//   Compact  < 600 dp   -> celular en vertical
//   Medium   600-839 dp -> tablet pequeña / celular plegable
//   Expanded >= 840 dp  -> tablet grande / escritorio
enum class TamanoVentana { Compact, Medium, Expanded }

// Función pura (sin Compose): fácil de probar con distintos anchos.
fun calcularTamanoVentana(anchoDp: Int): TamanoVentana = when {
    anchoDp < 600 -> TamanoVentana.Compact
    anchoDp < 840 -> TamanoVentana.Medium
    else -> TamanoVentana.Expanded
}

// Lee el ancho actual de la pantalla y se recalcula si cambia (ej.: al rotar el equipo).
@Composable
fun rememberTamanoVentana(): TamanoVentana {
    val anchoDp = LocalConfiguration.current.screenWidthDp
    return calcularTamanoVentana(anchoDp)
}
