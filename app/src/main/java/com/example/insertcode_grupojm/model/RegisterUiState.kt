package com.example.insertcode_grupojm.model

data class RegisterErrors(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null
)

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errors: RegisterErrors = RegisterErrors()
)