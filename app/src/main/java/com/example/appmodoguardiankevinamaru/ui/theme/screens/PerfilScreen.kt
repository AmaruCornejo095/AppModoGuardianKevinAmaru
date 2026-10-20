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
fun PerfilScreen(viewModel: UsuarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val usuario = uiState.usuarioAutenticado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Perfil de Usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nombre: ${usuario?.name ?: "No disponible"}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Correo: ${usuario?.email ?: "No disponible"}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rol: ${usuario?.role ?: "Operador"}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}