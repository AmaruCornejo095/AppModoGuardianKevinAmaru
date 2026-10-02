package com.example.appmodoguardiankevinamaru.model

// Modelo que almacena posibles errores individuales del formulario
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null
)

// Modelo principal que representa el estado del formulario del usuario
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val errores: UsuarioErrores = UsuarioErrores(),
    val aceptaTerminos: Boolean = false
)