package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ConfiguracionScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ListadoOfflineScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.LoginScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ModoGuardianScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.PerfilScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.RegistroScreen
import com.example.appmodoguardiankevinamaru.ui.theme.screens.ResumenScreen
import com.example.appmodoguardiankevinamaru.viewmodels.LoginViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel
import kotlinx.coroutines.launch

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object ModoGuardian : AppScreen("modo_guardian")
    object Perfil : AppScreen("perfil")
    object Configuracion : AppScreen("configuracion")
    object Registro : AppScreen("registro")
    object Resumen : AppScreen("resumen")
    object Listado : AppScreen("listado")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: UsuarioViewModel,
    loginViewModel: LoginViewModel = viewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val rutasNoAutenticadas = listOf(
        AppScreen.Login.route,
        AppScreen.Registro.route,
        AppScreen.Resumen.route
    )

    val drawerHabilitado = currentRoute !in rutasNoAutenticadas

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerHabilitado,
        drawerContent = {
            if (drawerHabilitado) {
                ModalDrawerSheet {
                    Text(
                        text = "Menú Principal",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))

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
                    NavigationDrawerItem(
                        label = { Text("Ir a Listado Offline") },
                        selected = currentRoute == AppScreen.Listado.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(AppScreen.Listado.route)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider()

                    NavigationDrawerItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
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
        }
    ) {
        Scaffold(
            topBar = {
                if (drawerHabilitado) {
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
                startDestination = AppScreen.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppScreen.Login.route) {
                    LoginScreen(
                        loginViewModel = loginViewModel,
                        onLoginSuccess = {
                            navController.navigate(AppScreen.ModoGuardian.route) {
                                popUpTo(AppScreen.Login.route) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(AppScreen.Registro.route)
                        }
                    )
                }
                composable(AppScreen.ModoGuardian.route) {
                    ModoGuardianScreen(viewModel = viewModel)
                }
                composable(AppScreen.Perfil.route) {
                    PerfilScreen(viewModel = viewModel)
                }
                composable(AppScreen.Configuracion.route) {
                    ConfiguracionScreen(viewModel = viewModel)
                }
                composable(AppScreen.Registro.route) {
                    RegistroScreen(
                        viewModel = viewModel,
                        onRegistroExitoso = {
                            navController.navigate(AppScreen.Resumen.route)
                        },
                        onNavigateToLogin = {
                            navController.navigate(AppScreen.Login.route) {
                                popUpTo(AppScreen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(AppScreen.Resumen.route) {
                    ResumenScreen(
                        viewModel = viewModel,
                        onNavigateToLogin = {
                            navController.navigate(AppScreen.Login.route) {
                                popUpTo(AppScreen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(AppScreen.Listado.route) {
                    ListadoOfflineScreen(viewModel = viewModel)
                }
            }
        }
    }
}