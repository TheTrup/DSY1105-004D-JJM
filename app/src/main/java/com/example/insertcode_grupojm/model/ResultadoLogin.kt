package com.example.insertcode_grupojm.model

// Posibles resultados de intentar iniciar sesión.
// Al ser sealed, el compilador obliga a manejar todos los casos en un "when".
sealed interface ResultadoLogin {
    data class Exito(val usuario: Usuario) : ResultadoLogin
    data object CredencialesInvalidas : ResultadoLogin
    data object SinConexion : ResultadoLogin
}
