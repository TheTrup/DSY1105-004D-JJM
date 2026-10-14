package com.example.insertcode_grupojm.model

// Roles disponibles en el sistema Modo Guardián.
// "etiqueta" es el texto que se muestra al usuario en pantalla.
enum class Rol(val etiqueta: String) {
    ADMIN("Administrador"),
    SUPERVISOR("Supervisor"),
    OPERADOR("Operador")
}
