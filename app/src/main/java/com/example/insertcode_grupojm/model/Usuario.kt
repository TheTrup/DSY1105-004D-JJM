package com.example.insertcode_grupojm.model

// Usuario autenticado. Nunca guarda la contraseña: solo correo y rol.
data class Usuario(
    val email: String,
    val rol: Rol
)
