package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

@Composable
fun ModoGuardianScreen(viewModel: UsuarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { viewModel.toggleModoEspecial(!uiState.modoEspecial) },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.modoEspecial) Color(0xFF4CAF50) else Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text(
                text = if (uiState.modoEspecial) "Desactivar" else "Activar",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}