package com.example.insertcode_grupojm.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.insertcode_grupojm.ui.components.HomeBase
import com.example.insertcode_grupojm.viewmodel.LoginViewModel

// Home del rol Operador. Observa el mismo LoginViewModel del login para conocer el rol.
@Composable
fun HomeOperadorScreen(
    loginViewModel: LoginViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by loginViewModel.estado.collectAsState()

    HomeBase(
        titulo = "Panel de Operador",
        estado = estado,
        onCerrarSesion = onCerrarSesion
    )
}
