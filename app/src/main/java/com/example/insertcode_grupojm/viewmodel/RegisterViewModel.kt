package com.example.insertcode_grupojm.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcode_grupojm.data.remote.RetrofitClient
import com.example.insertcode_grupojm.model.RegisterErrors
import com.example.insertcode_grupojm.model.RegisterRequest
import com.example.insertcode_grupojm.model.RegisterUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, errors = it.errors.copy(username = null)) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errors = it.errors.copy(email = null)) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errors = it.errors.copy(password = null)) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errors = it.errors.copy(confirmPassword = null)) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun validateForm(): Boolean {
        val s = _uiState.value

        val errUsername = when {
            s.username.trim().length < 3 -> "Mínimo 3 caracteres"
            else -> null
        }

        val errEmail = when {
            s.email.isBlank() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(s.email).matches() -> "Formato de correo no válido"
            else -> null
        }

        val errPassword = when {
            s.password.length < 6 -> "Mínimo 6 caracteres"
            else -> null
        }

        val errConfirm = when {
            s.confirmPassword.isEmpty() -> "Confirme su contraseña"
            s.confirmPassword != s.password -> "Las contraseñas no coinciden"
            else -> null
        }

        val errors = RegisterErrors(errUsername, errEmail, errPassword, errConfirm)
        _uiState.update { it.copy(errors = errors) }

        return listOfNotNull(errUsername, errEmail, errPassword, errConfirm).isEmpty()
    }

    fun submitRegistration(onSuccess: () -> Unit) {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val request = RegisterRequest(
                    username = _uiState.value.username,
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                RetrofitClient.apiService.registerUser(request)
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errors = it.errors.copy(email = "Error del servidor: ${e.localizedMessage}")
                    )
                }
            }
        }
    }

    fun registerWithApple(onLaunchAppleAuth: () -> Unit) {
        onLaunchAppleAuth()
    }
}