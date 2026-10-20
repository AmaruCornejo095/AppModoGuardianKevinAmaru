package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Resumen del Sistema",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Total de Usuarios Registrados:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${uiState.listaUsuarios.size}",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado del Modo Especial (DataStore):",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = if (uiState.modoEspecial) "ACTIVADO" else "DESACTIVADO",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}