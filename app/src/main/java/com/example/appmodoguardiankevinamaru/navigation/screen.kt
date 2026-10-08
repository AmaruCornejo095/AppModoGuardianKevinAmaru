package com.example.appmodoguardiankevinamaru.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object HomeAdmin : Screen("home_admin")
    data object HomeSupervisor : Screen("home_supervisor")
    data object HomeOperador : Screen("home_operador")
    data object Home : Screen("home_page")
    data object Profile : Screen("profile_page")
    data object Settings : Screen("settings_page")
    data object Registro : Screen("registro_page")
    data object Resumen : Screen("resumen_page")
}