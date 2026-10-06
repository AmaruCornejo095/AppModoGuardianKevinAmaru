package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmodoguardiankevinamaru.navigation.Screen
import com.example.appmodoguardiankevinamaru.viewmodels.MainViewModel

@Composable
fun HomeOperadorScreen(mainViewModel: MainViewModel) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Panel de Operador", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { mainViewModel.navigateTo(Screen.Home) }) {
                Text("Ir a App Principal (Modo Guardián)")
            }
        }
    }
}