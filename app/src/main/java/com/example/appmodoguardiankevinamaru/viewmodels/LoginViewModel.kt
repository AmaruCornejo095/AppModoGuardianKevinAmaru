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
            val updatedState = currentState.copy(email = newEmail, errores = errores, errorMessage = null)
            updatedState.copy(isLoginEnabled = validarFormulario(updatedState))
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { currentState ->
            val passError = if (newPassword.length >= 6 || newPassword.isEmpty()) null else "Mínimo 6 caracteres"
            val errores = currentState.errores.copy(passwordError = passError)
            val updatedState = currentState.copy(password = newPassword, errores = errores, errorMessage = null)
            updatedState.copy(isLoginEnabled = validarFormulario(updatedState))
        }
    }

    private fun validarFormulario(state: LoginUiState): Boolean {
        return state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                state.errores.emailError == null &&
                state.errores.passwordError == null
    }

    fun login(onSuccess: (String) -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        val user = authRepository.login(_uiState.value.email, _uiState.value.password)

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
}