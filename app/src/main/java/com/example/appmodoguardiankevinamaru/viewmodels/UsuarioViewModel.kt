package com.example.appmodoguardiankevinamaru.viewmodels

import androidx.lifecycle.ViewModel
import com.example.appmodoguardiankevinamaru.model.UsuarioErrores
import com.example.appmodoguardiankevinamaru.model.UsuarioUiState
import com.example.appmodoguardiankevinamaru.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado.asStateFlow()

    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    fun registrarConGoogle(onSuccess: () -> Unit) {
        val correo = "usuario.google@gmail.com"
        val nombre = "Usuario Google"

        authRepository.registerUser(
            email = correo,
            pass = "GoogleOAuth2026",
            name = nombre,
            role = "Operador"
        )

        _estado.update {
            it.copy(
                nombre = nombre,
                correo = correo,
                clave = "••••••••",
                direccion = "Autenticado con Google Account",
                aceptaTerminos = true
            )
        }
        onSuccess()
    }

    fun registrarConApple(onSuccess: () -> Unit) {
        val correo = "usuario.apple@icloud.com"
        val nombre = "Usuario Apple"

        authRepository.registerUser(
            email = correo,
            pass = "AppleOAuth2026",
            name = nombre,
            role = "Operador"
        )

        _estado.update {
            it.copy(
                nombre = nombre,
                correo = correo,
                clave = "••••••••",
                direccion = "Autenticado con Apple ID",
                aceptaTerminos = true
            )
        }
        onSuccess()
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo inválido" else null,
            clave = if (estadoActual.clave.length < 6) "Debe tener al menos 6 caracteres" else null,
            direccion = if (estadoActual.direccion.isBlank()) "Campo obligatorio" else null
        )

        val hayErroresCampos = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.direccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        if (hayErroresCampos || !estadoActual.aceptaTerminos) {
            return false
        }

        authRepository.registerUser(
            email = estadoActual.correo,
            pass = estadoActual.clave,
            name = estadoActual.nombre,
            role = "Operador"
        )

        return true
    }
}