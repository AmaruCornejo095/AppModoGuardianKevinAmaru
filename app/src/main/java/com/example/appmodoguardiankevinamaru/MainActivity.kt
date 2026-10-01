package com.example.appmodoguardiankevinamaru

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appmodoguardiankevinamaru.ui.theme.AppModoGuardianKevinAmaruTheme
import com.example.appmodoguardiankevinamaru.ui.theme.screens.HomeScreen2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppModoGuardianKevinAmaruTheme {
                HomeScreen2()
            }
        }
    }
}