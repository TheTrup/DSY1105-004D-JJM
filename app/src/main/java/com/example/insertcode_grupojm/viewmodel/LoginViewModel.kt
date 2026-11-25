package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import com.example.insertcode_grupojm.model.LoginErrores
import com.example.insertcode_grupojm.model.LoginUiState
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.model.Usuario
import com.example.insertcode_grupojm.repository.AuthRepository
import com.example.insertcode_grupojm.repository.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val preferencias: PreferenciasRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(
        LoginUiState(email = preferencias.obtenerUltimoEmail())
    )
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    // Almacena en memoria los usuarios creados en RegisterScreen durante la sesión de prueba
    private val usuariosCreados = mutableMapOf<String, Pair<String, Rol>>()

    fun onEmailChange(valor: String) {
        _estado.update {
            it.copy(email = valor, errores = it.errores.copy(email = null, general = null))
        }
    }

    fun onPasswordChange(valor: String) {
        _estado.update {
            it.copy(password = valor, errores = it.errores.copy(password = null, general = null))
        }
    }

    fun onSimularSinConexionChange(valor: Boolean) {
        _estado.update { it.copy(simularSinConexion = valor) }
    }

    fun registrarUsuarioLocal(email: String, password: String, rol: Rol = Rol.VENDEDOR) {
        val correoLimpio = email.trim().lowercase()
        usuariosCreados[correoLimpio] = Pair(password.trim(), rol)
        iniciarSesionOAuth(correoLimpio, rol)
    }

    fun onLoginClick(onSuccess: (Rol) -> Unit) {
        val emailLimpio = _estado.value.email.trim()
        val passwordLimpia = _estado.value.password.trim()

        val errorEmail = when {
            emailLimpio.isBlank() -> "Ingresa tu correo electrónico"
            !EMAIL_REGEX.matches(emailLimpio) -> "Correo electrónico no válido"
            else -> null
        }
        val errorPassword = when {
            passwordLimpia.isBlank() -> "Ingresa tu contraseña"
            passwordLimpia.length < LARGO_MINIMO_CLAVE -> "Mínimo $LARGO_MINIMO_CLAVE caracteres"
            else -> null
        }

        if (errorEmail != null || errorPassword != null) {
            _estado.update {
                it.copy(errores = LoginErrores(email = errorEmail, password = errorPassword))
            }
            return
        }

        val correoNormalizado = emailLimpio.lowercase()
        val usuarioRegistrado = usuariosCreados[correoNormalizado]

        val rolAsignado = when {
            usuarioRegistrado != null && usuarioRegistrado.first == passwordLimpia ->
                usuarioRegistrado.second
            correoNormalizado == "admin@insertcode.cl" && passwordLimpia == "123456" ->
                Rol.ADMIN
            correoNormalizado == "supervisor@insertcode.cl" && passwordLimpia == "123456" ->
                Rol.SUPERVISOR
            correoNormalizado == "vendedor@insertcode.cl" && passwordLimpia == "123456" ->
                Rol.VENDEDOR
            correoNormalizado == "repartidor@insertcode.cl" && passwordLimpia == "123456" ->
                Rol.REPARTIDOR
            else -> null
        }

        if (rolAsignado != null) {
            preferencias.guardarUltimoEmail(emailLimpio)
            val usuarioAutenticado = Usuario(email = emailLimpio, rol = rolAsignado)
            _estado.update {
                it.copy(
                    isLoading = false,
                    errores = LoginErrores(),
                    usuario = usuarioAutenticado
                )
            }
            onSuccess(rolAsignado)
        } else {
            _estado.update {
                it.copy(
                    isLoading = false,
                    errores = LoginErrores(
                        general = "Credenciales incorrectas. Usa un correo @insertcode.cl (clave 123456) o crea una cuenta."
                    )
                )
            }
        }
    }

    // Borra completamente la sesión, el correo guardado y la contraseña
    fun cerrarSesion() {
        preferencias.guardarUltimoEmail("")
        _estado.value = LoginUiState(email = "", password = "")
    }

    fun iniciarSesionOAuth(email: String, rolPorDefecto: Rol = Rol.VENDEDOR) {
        val correoLimpio = email.trim().ifBlank { "usuario.google@insertcode.cl" }
        preferencias.guardarUltimoEmail(correoLimpio)
        _estado.update {
            it.copy(
                email = correoLimpio,
                password = "",
                isLoading = false,
                errores = LoginErrores(),
                usuario = Usuario(email = correoLimpio, rol = rolPorDefecto)
            )
        }
    }

    fun actualizarRolSesion(nuevoRol: Rol) {
        _estado.update { current ->
            current.copy(
                usuario = current.usuario?.copy(rol = nuevoRol)
            )
        }
    }

    private companion object {
        const val LARGO_MINIMO_CLAVE = 6
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}