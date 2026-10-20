package com.example.appmodoguardiankevinamaru.viewmodels

import androidx.lifecycle.ViewModel
import com.example.appmodoguardiankevinamaru.model.LoginErrores
import com.example.appmodoguardiankevinamaru.model.LoginUiState
import com.example.appmodoguardiankevinamaru.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.update { currentState ->
            val emailError = if (newEmail.contains("@") || newEmail.isEmpty()) null else "Email inválido"
            val errores = currentState.errores.copy(emailError = emailError)
            currentState.copy(email = newEmail, errores = errores, errorMessage = null)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            val passError = if (newPassword.length >= 6 || newPassword.isEmpty()) null else "Mínimo 6 caracteres"
            val errores = currentState.errores.copy(passwordError = passError)
            currentState.copy(password = newPassword, errores = errores, errorMessage = null)
        }
    }

    fun login(onSuccess: (String) -> Unit) {
        val email = _uiState.value.email.trim()
        val pass = _uiState.value.password.trim()

        var emailErr: String? = null
        var passErr: String? = null

        if (email.isBlank()) {
            emailErr = "El correo es obligatorio"
        } else if (!email.contains("@")) {
            emailErr = "Email inválido"
        }

        if (pass.isBlank()) {
            passErr = "La contraseña es obligatoria"
        } else if (pass.length < 6) {
            passErr = "Mínimo 6 caracteres"
        }

        if (emailErr != null || passErr != null) {
            _uiState.update {
                it.copy(
                    errores = LoginErrores(emailError = emailErr, passwordError = passErr),
                    errorMessage = "Debe completar todos los campos correctamente"
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val user = authRepository.login(email, pass)

        if (user != null) {
            _uiState.update { it.copy(isLoading = false, userRole = user.role) }
            onSuccess(user.role)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Credenciales incorrectas"
                )
            }
        }
    }

    fun resetLoginState() {
        _uiState.value = LoginUiState()
    }
}