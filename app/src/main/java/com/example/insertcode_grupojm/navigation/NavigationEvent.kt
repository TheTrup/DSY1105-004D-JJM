package com.example.insertcode_grupojm.navigation

// Eventos de navegación que emite el NavigationViewModel y consume AppNavigation.
sealed class NavigationEvent {
    // popUpTo: ruta hasta la que se limpia la pila (null = no limpia nada).
    data class NavigateTo(
        val route: String,
        val popUpTo: String? = null,
        val inclusive: Boolean = false
    ) : NavigationEvent()

    data object NavigateBack : NavigationEvent()
}
