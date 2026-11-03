package com.example.appmodoguardiankevinamaru.ui.theme.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmodoguardiankevinamaru.navigation.AppScreen
import com.example.appmodoguardiankevinamaru.viewmodels.UsuarioViewModel

@Composable
fun GoogleIcon(modifier: Modifier = Modifier.size(20.dp)) {
    Canvas(modifier = modifier) {
        val scaleX = size.width / 24f
        val scaleY = size.height / 24f
        scale(scaleX, scaleY, pivot = Offset.Zero) {
            // Azul
            drawPath(
                path = PathParser().parsePathString("M23.49 12.28c0-.79-.07-1.54-.19-2.28H12v4.51h6.47c-.29 1.48-1.14 2.73-2.4 3.58v3h3.86c2.26-2.09 3.56-5.17 3.56-8.81z").toPath(),
                color = Color(0xFF4285F4)
            )
            // Verde
            drawPath(
                path = PathParser().parsePathString("M12 24c3.24 0 5.95-1.08 7.93-2.91l-3.86-3c-1.08.72-2.45 1.16-4.07 1.16-3.13 0-5.78-2.11-6.73-4.96H1.29v3.09C3.26 21.3 7.31 24 12 24z").toPath(),
                color = Color(0xFF34A853)
            )
            // Amarillo
            drawPath(
                path = PathParser().parsePathString("M5.27 14.29c-.25-.72-.38-1.49-.38-2.29s.14-1.57.38-2.29V6.62H1.29C.47 8.24 0 10.06 0 12s.47 3.76 1.29 5.38l3.98-3.09z").toPath(),
                color = Color(0xFFFBBC05)
            )
            // Rojo
            drawPath(
                path = PathParser().parsePathString("M12 4.75c1.77 0 3.35.61 4.6 1.8l3.42-3.42C17.95 1.19 15.24 0 12 0 7.31 0 3.26 2.7 1.29 6.62l3.98 3.09c.95-2.85 3.6-4.96 6.73-4.96z").toPath(),
                color = Color(0xFFEA4335)
            )
        }
    }
}

@Composable
fun AppleIcon(modifier: Modifier = Modifier.size(20.dp), tint: Color = MaterialTheme.colorScheme.onSurface) {
    Canvas(modifier = modifier) {
        val scaleX = size.width / 24f
        val scaleY = size.height / 24f
        scale(scaleX, scaleY, pivot = Offset.Zero) {
            drawPath(
                path = PathParser().parsePathString("M18.71 19.5c-.83 1.24-1.71 2.45-3.05 2.47-1.34.03-1.77-.79-3.29-.79-1.53 0-2 .77-3.27.82-1.31.05-2.3-1.32-3.14-2.53C4.25 17 2.94 12.45 4.7 9.39c.87-1.52 2.43-2.48 4.12-2.51 1.28-.02 2.5.87 3.29.87.78 0 2.26-1.07 3.81-.91.65.03 2.47.26 3.64 1.98-.09.06-2.17 1.28-2.15 3.81.03 3.02 2.65 4.03 2.68 4.04-.03.07-.42 1.44-1.38 2.83M15.97 4.54c.64-.78 1.08-1.85.96-2.92-.93.04-2.06.62-2.73 1.4-.6.69-1.12 1.79-.98 2.85 1.04.08 2.11-.55 2.75-1.33z").toPath(),
                color = tint
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val context = LocalContext.current
    val estado by viewModel.estado.collectAsState()

    var visible by remember { mutableStateOf(true) }
    var mostrarExito by remember { mutableStateOf(false) }
    var mostrarErrorTerminos by remember { mutableStateOf(false) }

    var confirmarClave by remember { mutableStateOf("") }
    var errorConfirmarClave by remember { mutableStateOf<String?>(null) }

    if (mostrarExito) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF1E8E3E),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "¡Cuenta creada con éxito!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "Tu cuenta se ha registrado correctamente en Modo Guardián.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarExito = false
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo(AppScreen.Registro.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8E3E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a Iniciar Sesión", color = Color.White)
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = { navController.navigate(AppScreen.Login.route) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(600)) + slideInVertically(
                initialOffsetY = { 40 },
                animationSpec = tween(600)
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Registrarse en Modo Guardián",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/ServiceLogin"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            GoogleIcon(modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Continuar con Google",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://appleid.apple.com/sign-in"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AppleIcon(modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Continuar con Apple",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        Text(
                            text = " o ",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Correo electrónico *",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = estado.correo,
                        onValueChange = viewModel::onCorreoChange,
                        placeholder = { Text("Correo electrónico") },
                        isError = estado.errores.correo != null,
                        supportingText = {
                            estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Contraseña *",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = estado.clave,
                        onValueChange = viewModel::onClaveChange,
                        placeholder = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = estado.errores.clave != null,
                        supportingText = {
                            estado.errores.clave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Confirmar contraseña *",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = confirmarClave,
                        onValueChange = {
                            confirmarClave = it
                            errorConfirmarClave = null
                        },
                        placeholder = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        isError = errorConfirmarClave != null,
                        supportingText = {
                            errorConfirmarClave?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = estado.aceptaTerminos,
                            onCheckedChange = {
                                viewModel.onAceptaTerminosChange(it)
                                if (it) mostrarErrorTerminos = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Acepto los términos y condiciones de servicio",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (mostrarErrorTerminos && !estado.aceptaTerminos) {
                        Text(
                            text = "Debes aceptar los términos y condiciones para crear la cuenta",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.onNombreChange("Usuario")
                            viewModel.onDireccionChange("Sin dirección")

                            if (!estado.aceptaTerminos) {
                                mostrarErrorTerminos = true
                            }

                            var coincidenciaValida = true
                            if (confirmarClave.isBlank()) {
                                errorConfirmarClave = "Campo obligatorio"
                                coincidenciaValida = false
                            } else if (confirmarClave != estado.clave) {
                                errorConfirmarClave = "Las contraseñas no coinciden"
                                coincidenciaValida = false
                            } else {
                                errorConfirmarClave = null
                            }

                            val formValido = viewModel.validarFormulario()
                            if (formValido && coincidenciaValida && estado.aceptaTerminos) {
                                mostrarErrorTerminos = false
                                mostrarExito = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E8E3E)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Crear cuenta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}