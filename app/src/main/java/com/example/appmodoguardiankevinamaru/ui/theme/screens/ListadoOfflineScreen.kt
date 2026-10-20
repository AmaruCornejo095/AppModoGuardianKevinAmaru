package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

@Composable
fun ListadoOfflineScreen(viewModel: UsuarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.listaUsuarios.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay usuarios registrados aún.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.listaUsuarios) { usuario ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = usuario.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(text = usuario.correo, style = MaterialTheme.typography.bodySmall)
                            Text(text = usuario.direccion, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.eliminarUsuario(usuario) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar Usuario"
                            )
                        }
                    }
                }
            }
        }
    }
}