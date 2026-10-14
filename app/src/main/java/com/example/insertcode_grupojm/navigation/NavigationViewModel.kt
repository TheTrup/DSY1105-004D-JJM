package com.example.insertcode_grupojm.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// Las pantallas piden navegar a este ViewModel; no tocan el NavController directamente.
class NavigationViewModel : ViewModel() {

    // SharedFlow (no StateFlow): un evento de navegación ocurre una vez y no debe repetirse.
    private val _eventos = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 8)
    val eventos: SharedFlow<NavigationEvent> = _eventos.asSharedFlow()

    fun navigateTo(route: String, popUpTo: String? = null, inclusive: Boolean = false) {
        _eventos.tryEmit(NavigationEvent.NavigateTo(route, popUpTo, inclusive))
    }

    fun navigateBack() {
        _eventos.tryEmit(NavigationEvent.NavigateBack)
    }
}
