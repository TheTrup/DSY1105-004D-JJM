package com.example.insertcode_grupojm.ui.screens

import androidx.compose.runtime.Composable
import com.example.insertcode_grupojm.ui.utils.TamanoVentana
import com.example.insertcode_grupojm.ui.utils.rememberTamanoVentana

@Composable
fun HomeScreenAdaptive() {
    val tamano = rememberTamanoVentana()

    when (tamano) {
        TamanoVentana.Compact -> HomeScreenCompacta()
        TamanoVentana.Medium -> HomeScreenMediana()
        TamanoVentana.Expanded -> HomeScreenExpandida()
    }
}