package com.example.insertcode_grupojm.model

import android.net.Uri

// Modelo con errores individuales para la validación del Módulo Integral (Guía 14)
data class UsuarioIntegralErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val terminos: String? = null,
    val foto: String? = null
)

// Estado de UI para el Módulo Integral
data class UsuarioIntegralUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val fotoUri: Uri? = null,
    val modoEspecial: Boolean = false,
    val errores: UsuarioIntegralErrores = UsuarioIntegralErrores()
)