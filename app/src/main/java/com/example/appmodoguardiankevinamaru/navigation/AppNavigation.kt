package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object ModoGuardian : AppScreen("modo_guardian")
    object Perfil : AppScreen("perfil")
    object Configuracion : AppScreen("configuracion")
    object Registro : AppScreen("registro")
    object Resumen : AppScreen("resumen")
    object PanelPrincipal : AppScreen("panel_principal")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: UsuarioViewModel = viewModel()) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 1. Fallback inicial configurado en Login
    val currentRoute = navBackStackEntry?.destination?.route ?: AppScreen.Login.route

    val mostrarDrawer = currentRoute != AppScreen.Login.route && currentRoute != AppScreen.Registro.route

    ModalNavigationDrawer(
        gesturesEnabled = mostrarDrawer,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú Principal",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                NavigationDrawerItem(
                    label = { Text("Ir a Panel Principal") },
                    selected = currentRoute == AppScreen.PanelPrincipal.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.PanelPrincipal.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Modo Guardián") },
                    selected = currentRoute == AppScreen.ModoGuardian.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.ModoGuardian.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Perfil") },
                    selected = currentRoute == AppScreen.Perfil.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.Perfil.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Configuración") },
                    selected = currentRoute == AppScreen.Configuracion.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.Configuracion.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Registro") },
                    selected = currentRoute == AppScreen.Registro.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.Registro.route)
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Ir a Resumen") },
                    selected = currentRoute == AppScreen.Resumen.route,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.Resumen.route)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider()

                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión"
                        )
                    },
                    label = { Text("Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (mostrarDrawer) {
                    TopAppBar(
                        title = { Text("Modo Guardián") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                // 2. Destino inicial configurado en Login
                startDestination = AppScreen.Login.route,
                modifier = Modifier.padding(if (mostrarDrawer) innerPadding else PaddingValues(0.dp))
            ) {
                composable(AppScreen.Login.route) {
                    val loginViewModel: LoginViewModel = viewModel()
                    LoginScreen(
                        loginViewModel = loginViewModel,
                        onNavigateToRegister = { navController.navigate(AppScreen.Registro.route) },
                        onLoginSuccess = { navController.navigate(AppScreen.PanelPrincipal.route) }
                    )
                }
                composable(AppScreen.Registro.route) {
                    RegistroScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(AppScreen.PanelPrincipal.route) {
                    PantallaPrincipal()
                }
                composable(AppScreen.ModoGuardian.route) {
                    HomeScreen()
                }
                composable(AppScreen.Perfil.route) {
                    val mainViewModel: MainViewModel = viewModel()
                    ProfileScreen(
                        viewModel = mainViewModel,
                        onOpenDrawer = { scope.launch { drawerState.open() } }
                    )
                }
                composable(AppScreen.Configuracion.route) {
                    val mainViewModel: MainViewModel = viewModel()
                    SettingsScreen(viewModel = mainViewModel)
                }
                composable(AppScreen.Resumen.route) {
                    ResumenScreen(viewModel = viewModel)
                }
            }
        }
    }
}