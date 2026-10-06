package com.example.appmodoguardiankevinamaru.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class WindowSizeClass { Compact, Medium, Expanded }

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    return when {
        screenWidth < 600 -> WindowSizeClass.Compact
        screenWidth < 840 -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }
}