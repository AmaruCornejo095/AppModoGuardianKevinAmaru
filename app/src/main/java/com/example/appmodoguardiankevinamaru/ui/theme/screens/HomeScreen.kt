package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appmodoguardiankevinamaru.R

// 1. Uso de API experimental para Scaffold y TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // 2. Estructura visual base con barra superior
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Modo Guardián") })
        }
    ) { innerPadding ->
        // 3. Contenedor vertical con espaciado uniforme (verticalArrangement) y alineación centrada
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 4. Texto principal con estilo de MaterialTheme
            Text(
                text = "¡Bienvenido a Modo Guardián!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // 5. Elemento visual adicional para comprobar funcionamiento
            Text(
                text = "Estado del sistema: Listo",
                style = MaterialTheme.typography.bodyMedium
            )

            // 6. Botón de acción
            Button(onClick = { /* Acción futura */ }) {
                Text("Presionar")
            }

            // 7. Imagen (utiliza el recurso básico del sistema para evitar errores)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// 8. Vista previa en Android Studio
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}