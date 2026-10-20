package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmodoguardiankevinamaru.model.UsuarioEntity
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onRegistroExitoso: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.modoEspecial,
                onCheckedChange = { viewModel.toggleModoEspecial(it) }
            )
            Text(text = "Activar Modo Especial")
        }

        Button(
            onClick = {
                if (nombre.isNotBlank() && correo.isNotBlank()) {
                    val nuevoUsuario = UsuarioEntity(
                        nombre = nombre,
                        correo = correo,
                        direccion = direccion,
                        fotoPath = "",
                        modoEspecial = uiState.modoEspecial
                    )
                    viewModel.guardarUsuario(nuevoUsuario)
                    onRegistroExitoso()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Usuario")
        }
    }
}