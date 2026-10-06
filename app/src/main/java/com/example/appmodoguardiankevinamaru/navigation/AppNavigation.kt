package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardiankevinamaru.ui.theme.screens.HomeScreen2
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ProfileScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.RegistroScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ResumenScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.SettingsScreen
import com.example.appmodoguardiankevinamaru.viewmodels.MainViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popupToRoute?.let {
                            popUpTo(it.route) {
                                inclusive = event.inclusive
                            }
                        }
                        launchSingleTop = event.singleTop
                    }
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen2(viewModel = mainViewModel)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(viewModel = mainViewModel)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(viewModel = mainViewModel)
        }
        composable(route = Screen.Registro.route) {
            RegistroScreen(navController = navController, viewModel = usuarioViewModel)
        }
        composable(route = Screen.Resumen.route) {
            ResumenScreen(viewModel = usuarioViewModel)
        }
    }
}