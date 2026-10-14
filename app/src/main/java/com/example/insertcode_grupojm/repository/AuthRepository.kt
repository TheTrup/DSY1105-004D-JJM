package com.example.insertcode_grupojm.repository

import com.example.insertcode_grupojm.model.ResultadoLogin
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.model.Usuario
import kotlinx.coroutines.delay

// Repositorio de autenticación con datos de prueba (sintéticos, nunca credenciales reales).
class AuthRepository {

    // Credencial interna: incluye la clave, por eso no se expone fuera del repositorio.
    private data class Credencial(val email: String, val password: String, val rol: Rol)

    private val credenciales = listOf(
        Credencial("admin@guardian.test", "123456", Rol.ADMIN),
        Credencial("supervisor@guardian.test", "123456", Rol.SUPERVISOR),
        Credencial("operador@guardian.test", "123456", Rol.OPERADOR)
    )

    // Simula una consulta a un servidor: espera un momento y luego responde.
    // simularSinConexion permite probar el manejo de error de conectividad.
    suspend fun autenticar(
        email: String,
        password: String,
        simularSinConexion: Boolean = false
    ): ResultadoLogin {
        delay(RETARDO_SIMULADO_MS)

        if (simularSinConexion) return ResultadoLogin.SinConexion

        val emailNormalizado = email.trim().lowercase()
        val coincidencia = credenciales.firstOrNull {
            it.email == emailNormalizado && it.password == password
        }

        return if (coincidencia != null) {
            ResultadoLogin.Exito(Usuario(coincidencia.email, coincidencia.rol))
        } else {
            ResultadoLogin.CredencialesInvalidas
        }
    }

    private companion object {
        const val RETARDO_SIMULADO_MS = 1500L
    }
}
