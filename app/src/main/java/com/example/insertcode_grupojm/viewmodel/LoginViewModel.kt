package com.example.insertcode_grupojm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.model.LoginErrores
import com.example.insertcode_grupojm.model.LoginUiState
import com.example.insertcode_grupojm.model.ResultadoLogin
import com.example.insertcode_grupojm.model.Rol
import com.example.insertcode_grupojm.repository.AuthRepository
import com.example.insertcode_grupojm.repository.PreferenciasRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel del login. Una única instancia (ámbito Activity) la comparten
// LoginScreen y los Home, así los Home conocen el rol sin recibir argumentos.
class LoginViewModel(
    private val authRepository: AuthRepository,
    private val preferencias: PreferenciasRepository
) : ViewModel() {

    // El correo parte con el último usado (persistencia local limitada).
    private val _estado = MutableStateFlow(
        LoginUiState(email = preferencias.obtenerUltimoEmail())
    )
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    // Al escribir se limpian los errores del campo y el error general.
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

    // Valida el formato de los campos y publica los errores en el estado.
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val correo = actual.email.trim()

        val errores = LoginErrores(
            email = when {
                correo.isBlank() -> "Campo obligatorio"
                !EMAIL_REGEX.matches(correo) -> "Correo inválido"
                else -> null
            },
            password = when {
                actual.password.isBlank() -> "Campo obligatorio"
                actual.password.length < LARGO_MINIMO_CLAVE -> "Mínimo $LARGO_MINIMO_CLAVE caracteres"
                else -> null
            }
        )

        _estado.update { it.copy(errores = errores) }
        return errores.email == null && errores.password == null
    }

    // Inicia sesión. Si resulta bien, avisa el rol mediante onExito para que
    // la pantalla pida la navegación (el ViewModel no conoce el NavController).
    fun onLoginClick(onExito: (Rol) -> Unit) {
        if (_estado.value.isLoading || !validarFormulario()) return

        _estado.update { it.copy(isLoading = true, errores = it.errores.copy(general = null)) }

        viewModelScope.launch {
            val actual = _estado.value
            try {
                when (
                    val resultado = authRepository.autenticar(
                        email = actual.email,
                        password = actual.password,
                        simularSinConexion = actual.simularSinConexion
                    )
                ) {
                    is ResultadoLogin.Exito -> {
                        preferencias.guardarUltimoEmail(resultado.usuario.email)
                        _estado.update {
                            it.copy(isLoading = false, password = "", usuario = resultado.usuario)
                        }
                        onExito(resultado.usuario.rol)
                    }
                    ResultadoLogin.CredencialesInvalidas ->
                        mostrarErrorGeneral("Correo o contraseña incorrectos")
                    ResultadoLogin.SinConexion ->
                        mostrarErrorGeneral("Sin conexión. Revisa tu red e inténtalo de nuevo")
                }
            } catch (e: CancellationException) {
                throw e // Nunca se debe tragar la cancelación de una corrutina
            } catch (e: Exception) {
                // Un error inesperado no debe cerrar la app: se informa y se permite reintentar.
                mostrarErrorGeneral("Ocurrió un error inesperado. Inténtalo nuevamente")
            }
        }
    }

    // Cierra la sesión: borra el usuario, la clave y los errores (el correo se conserva).
    fun cerrarSesion() {
        _estado.update {
            it.copy(password = "", usuario = null, isLoading = false, errores = LoginErrores())
        }
    }

    private fun mostrarErrorGeneral(mensaje: String) {
        _estado.update {
            it.copy(isLoading = false, errores = it.errores.copy(general = mensaje))
        }
    }

    private companion object {
        const val LARGO_MINIMO_CLAVE = 6
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
