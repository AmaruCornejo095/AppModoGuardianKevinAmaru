package com.example.appmodoguardiankevinamaru.model

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val errores: LoginErrores = LoginErrores(),
    val userRole: String? = null,
    val errorMessage: String? = null
)