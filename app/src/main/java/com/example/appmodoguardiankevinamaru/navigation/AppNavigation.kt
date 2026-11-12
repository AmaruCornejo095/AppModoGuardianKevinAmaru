package com.example.appmodoguardiankevinamaru.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardiankevinamaru.ui.theme.screens.*
import com.example.appmodoguardiankevinamaru.viewmodels.LoginViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.MainViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

sealed class AppScreen(val route: String) {
    object Login : AppScreen("login")
    object Registro : AppScreen("registro")
    object PanelPrincipal : AppScreen("panel_principal")
    object Camaras : AppScreen("camaras")
    object Eventos : AppScreen("eventos")
    object Alertas : AppScreen("alertas")
    object Perfil : AppScreen("perfil")
    object ModoGuardian : AppScreen("modo_guardian")
    object Configuracion : AppScreen("configuracion")
    object Resumen : AppScreen("resumen")
}

private data class BottomMenuItem(
    val etiqueta: String,
    val ruta: String,
    val icono: ImageVector,
    val badgeCount: Int = 0
)

private val LightGuardianColors = lightColorScheme(
    primary = Color(0xFF1A56DB),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E3A8A),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outlineVariant = Color(0xFFCBD5E1)
)

private val DarkGuardianColors = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    background = Color(0xFF0B1120),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFF94A3B8),
    outlineVariant = Color(0xFF334155)
)

@Composable
fun AppNavigation(viewModel: UsuarioViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AppScreen.Login.route

    val sesionGuardian = remember { SesionGuardianState() }
    val estadoRegistro by viewModel.estado.collectAsState()

    val mostrarBottomBar = currentRoute != AppScreen.Login.route && currentRoute != AppScreen.Registro.route
    val cantidadAlertasActivas = if (sesionGuardian.alertaActiva != null) 1 else 0

    // Navegación limpia entre secciones sin apilar pantallas duplicadas
    fun navegarAPantalla(ruta: String) {
        if (currentRoute == ruta) return
        if (ruta == AppScreen.PanelPrincipal.route) {
            navController.popBackStack(AppScreen.PanelPrincipal.route, inclusive = false)
        } else {
            navController.navigate(ruta) {
                popUpTo(AppScreen.PanelPrincipal.route) {
                    saveState = false
                }
                launchSingleTop = true
                restoreState = false
            }
        }
    }

    val menuItems = listOf(
        BottomMenuItem("Inicio", AppScreen.PanelPrincipal.route, Icons.Default.Home),
        BottomMenuItem("Cámaras", AppScreen.Camaras.route, Icons.Default.PlayArrow),
        BottomMenuItem("Eventos", AppScreen.Eventos.route, Icons.AutoMirrored.Filled.List),
        BottomMenuItem("Alertas", AppScreen.Alertas.route, Icons.Default.Notifications, badgeCount = cantidadAlertasActivas),
        BottomMenuItem("Perfil", AppScreen.Perfil.route, Icons.Default.Person)
    )

    val esquemaColores = if (sesionGuardian.modoOscuro && mostrarBottomBar) {
        DarkGuardianColors
    } else {
        LightGuardianColors
    }

    MaterialTheme(colorScheme = esquemaColores) {
        Scaffold(
            bottomBar = {
                if (mostrarBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 6.dp
                    ) {
                        menuItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.ruta,
                                onClick = { navegarAPantalla(item.ruta) },
                                icon = {
                                    if (item.badgeCount > 0) {
                                        BadgedBox(
                                            badge = {
                                                Badge(containerColor = Color(0xFFD93025)) {
                                                    Text(
                                                        text = item.badgeCount.toString(),
                                                        color = Color.White,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }
                                        ) {
                                            Icon(item.icono, contentDescription = item.etiqueta)
                                        }
                                    } else {
                                        Icon(item.icono, contentDescription = item.etiqueta)
                                    }
                                },
                                label = { Text(item.etiqueta, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppScreen.Login.route,
                modifier = Modifier.padding(if (mostrarBottomBar) innerPadding else PaddingValues(0.dp))
            ) {
                composable(AppScreen.Login.route) {
                    val loginViewModel: LoginViewModel = viewModel()
                    LoginScreen(
                        loginViewModel = loginViewModel,
                        onNavigateToRegister = { navController.navigate(AppScreen.Registro.route) },
                        onLoginSuccess = {
                            val correoDetectado = extraerCorreoDeSesion(loginViewModel)
                                ?: estadoRegistro.correo.takeIf { it.contains("@") }
                                ?: "usuario@modoguardian.cl"
                            val nombreDetectado = estadoRegistro.nombre.takeIf { it.isNotBlank() && it != "Usuario" }

                            sesionGuardian.iniciarSesionConUsuario(
                                correo = correoDetectado,
                                nombreRegistrado = nombreDetectado
                            )

                            navController.navigate(AppScreen.PanelPrincipal.route) {
                                popUpTo(AppScreen.Login.route) { inclusive = true }
                            }
                        }
                    )
                }
                composable(AppScreen.Registro.route) {
                    RegistroScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(AppScreen.PanelPrincipal.route) {
                    InicioGuardianScreen(
                        sesion = sesionGuardian,
                        onNavigateToCamaras = { navegarAPantalla(AppScreen.Camaras.route) },
                        onNavigateToEventos = { navegarAPantalla(AppScreen.Eventos.route) },
                        onNavigateToAlertas = { navegarAPantalla(AppScreen.Alertas.route) }
                    )
                }
                composable(AppScreen.Camaras.route) {
                    CamarasScreen(sesion = sesionGuardian)
                }
                composable(AppScreen.Eventos.route) {
                    EventosScreen(sesion = sesionGuardian)
                }
                composable(AppScreen.Alertas.route) {
                    AlertasScreen(sesion = sesionGuardian)
                }
                composable(AppScreen.Perfil.route) {
                    PerfilGuardianScreen(
                        sesion = sesionGuardian,
                        onNavigateToInicio = { navegarAPantalla(AppScreen.PanelPrincipal.route) },
                        onNavigateToCamaras = { navegarAPantalla(AppScreen.Camaras.route) },
                        onNavigateToEventos = { navegarAPantalla(AppScreen.Eventos.route) },
                        onCerrarSesion = {
                            sesionGuardian.cerrarSesion()
                            navController.navigate(AppScreen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
                composable(AppScreen.ModoGuardian.route) {
                    HomeScreen()
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