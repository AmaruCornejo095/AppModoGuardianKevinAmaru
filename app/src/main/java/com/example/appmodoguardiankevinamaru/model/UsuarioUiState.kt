package com.example.appmodoguardiankevinamaru.model

import android.net.Uri

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val fotoUri: Uri? = null,
    val modoEspecial: Boolean = false
)