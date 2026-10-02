package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardiankevinamaru.ui.theme.screens.RegistroScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ResumenScreen
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Instancia compartida del ViewModel entre ambas pantallas
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "registro"
    ) {
        composable(route = "registro") {
            RegistroScreen(navController = navController, viewModel = usuarioViewModel)
        }
        composable(route = "resumen") {
            ResumenScreen(viewModel = usuarioViewModel)
        }
    }
}