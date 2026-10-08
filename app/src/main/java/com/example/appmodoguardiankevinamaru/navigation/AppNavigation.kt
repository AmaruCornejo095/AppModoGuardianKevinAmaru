package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardiankevinamaru.ui.theme.screens.*
import com.example.appmodoguardiankevinamaru.viewmodels.LoginViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.MainViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()

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
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = { role ->
                    when (role) {
                        "Admin" -> navController.navigate(Screen.HomeAdmin.route)
                        "Supervisor" -> navController.navigate(Screen.HomeSupervisor.route)
                        "Operador" -> navController.navigate(Screen.HomeOperador.route)
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Registro.route)
                }
            )
        }

        composable(route = Screen.HomeAdmin.route) {
            HomeAdminScreen(mainViewModel = mainViewModel)
        }
        composable(route = Screen.HomeSupervisor.route) {
            HomeSupervisorScreen(mainViewModel = mainViewModel)
        }
        composable(route = Screen.HomeOperador.route) {
            HomeOperadorScreen(mainViewModel = mainViewModel)
        }

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