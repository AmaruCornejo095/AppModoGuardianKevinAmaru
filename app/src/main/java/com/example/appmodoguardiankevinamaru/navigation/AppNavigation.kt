package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardiankevinamaru.ui.theme.screens.*
import com.example.appmodoguardiankevinamaru.viewmodels.LoginViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.MainViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    usuarioViewModel: UsuarioViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route
    val gesturesEnabled = rutaActual != Screen.Login.route && rutaActual != Screen.Registro.route

    val handleLogout = {
        loginViewModel.resetLoginState()
        navController.navigate(Screen.Login.route) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
        }
    }

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú Principal",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Ir a Modo Guardián") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Estado.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Profile.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Configuración") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Settings.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Registro") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Registro.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Resumen") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(Screen.Resumen.route)
                    }
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        handleLogout()
                    }
                )
            }
        }
    ) {
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
                HomeAdminScreen(
                    mainViewModel = mainViewModel,
                    onLogout = handleLogout
                )
            }
            composable(route = Screen.HomeSupervisor.route) {
                HomeSupervisorScreen(
                    mainViewModel = mainViewModel,
                    onLogout = handleLogout
                )
            }
            composable(route = Screen.HomeOperador.route) {
                HomeOperadorScreen(
                    mainViewModel = mainViewModel,
                    onLogout = handleLogout
                )
            }

            composable(route = Screen.Home.route) {
                HomeScreen2(
                    viewModel = mainViewModel,
                    onLogout = handleLogout
                )
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    viewModel = mainViewModel,
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
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
            composable(route = Screen.Estado.route) {
                PantallaPrincipal()
            }
        }
    }
}