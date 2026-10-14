package com.example.insertcode_grupojm.model

// Estado completo de la pantalla de login.
// Lo observan LoginScreen y las pantallas Home (para conocer el rol).
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val simularSinConexion: Boolean = false,
    val errores: LoginErrores = LoginErrores(),
    val usuario: Usuario? = null
) {
    // El botón solo se habilita con ambos campos completos y sin una carga en curso.
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading

    // Rol de la sesión activa (null si no hay sesión).
    val rol: Rol?
        get() = usuario?.rol
}
