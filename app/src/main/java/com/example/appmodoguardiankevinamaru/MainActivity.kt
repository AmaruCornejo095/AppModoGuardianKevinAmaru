package com.example.appmodoguardiankevinamaru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.appmodoguardiankevinamaru.data.AppDatabase
import com.example.appmodoguardiankevinamaru.data.EstadoDataStore
import com.example.appmodoguardiankevinamaru.navigation.AppNavigation
import com.example.appmodoguardiankevinamaru.ui.theme.AppModoGuardianKevinAmaruTheme
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val dataStore = EstadoDataStore(applicationContext)
        val factory = UsuarioViewModelFactory(database.usuarioDao(), dataStore)
        val viewModel = ViewModelProvider(this, factory)[UsuarioViewModel::class.java]

        setContent {
            AppModoGuardianKevinAmaruTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}