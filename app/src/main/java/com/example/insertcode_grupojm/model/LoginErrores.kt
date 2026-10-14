package com.example.insertcode_grupojm.model

// Errores del formulario de login.
// email y password: errores de validación por campo.
// general: errores de autenticación o conectividad (se muestran sobre el botón).
data class LoginErrores(
    val email: String? = null,
    val password: String? = null,
    val general: String? = null
)
