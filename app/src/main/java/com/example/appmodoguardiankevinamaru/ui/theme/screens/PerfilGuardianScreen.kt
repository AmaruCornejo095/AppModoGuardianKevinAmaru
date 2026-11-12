package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PerfilGuardianScreen(
    sesion: SesionGuardianState,
    onNavigateToInicio: () -> Unit = {},
    onNavigateToCamaras: () -> Unit = {},
    onNavigateToEventos: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val infoTurno by rememberRelojTurnoActual()

    // Estados de diálogos interactivos
    var editarPerfilDialog by remember { mutableStateOf(false) }
    var mostrarTurnoDialog by remember { mutableStateOf(false) }
    var mostrarCredencialDialog by remember { mutableStateOf(false) }
    var mostrarCambiarClaveDialog by remember { mutableStateOf(false) }
    var mostrarAyudaDialog by remember { mutableStateOf(false) }
    var mostrarTerminosDialog by remember { mutableStateOf(false) }
    var mensajeConfirmacion by remember { mutableStateOf<String?>(null) }

    var nuevoNombre by remember(sesion.nombreUsuario) { mutableStateOf(sesion.nombreUsuario) }
    var nuevaZona by remember(sesion.zonaAsignada) { mutableStateOf(sesion.zonaAsignada) }

    // Campos de cambio de contraseña
    var claveActual by remember { mutableStateOf("") }
    var claveNueva by remember { mutableStateOf("") }
    var claveConfirmar by remember { mutableStateOf("") }
    var errorClave by remember { mutableStateOf<String?>(null) }

    val verde = Color(0xFF1E8E3E)
    val rojo = Color(0xFFD93025)

    // 1. Diálogo Editar Perfil
    if (editarPerfilDialog) {
        AlertDialog(
            onDismissRequest = { editarPerfilDialog = false },
            title = { Text("Editar datos de perfil", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = nuevaZona,
                        onValueChange = { nuevaZona = it },
                        label = { Text("Zona asignada") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (nuevoNombre.isNotBlank()) sesion.nombreUsuario = nuevoNombre.trim()
                    if (nuevaZona.isNotBlank()) sesion.zonaAsignada = nuevaZona.trim()
                    mensajeConfirmacion = "Perfil actualizado correctamente"
                    editarPerfilDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { editarPerfilDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 2. Diálogo Detalle de Turno Activo
    if (mostrarTurnoDialog) {
        AlertDialog(
            onDismissRequest = { mostrarTurnoDialog = false },
            title = { Text("Estado del Turno Activo", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Operador: ${sesion.nombreUsuario}")
                    Text("Zona asignada: ${sesion.zonaAsignada}")
                    Text("Horario: ${infoTurno.horarioTurno}")
                    Text("Progreso actual: ${infoTurno.porcentajeTexto} (${infoTurno.tiempoRestanteTexto})")
                    Text("Estado: Sincronizado con Central", color = verde, fontWeight = FontWeight.SemiBold)
                }
            },
            confirmButton = {
                Button(onClick = { mostrarTurnoDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // 3. Diálogo Certificado / Credencial Digital
    if (mostrarCredencialDialog) {
        AlertDialog(
            onDismissRequest = { mostrarCredencialDialog = false },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = verde, modifier = Modifier.size(40.dp))
            },
            title = { Text("Credencial Digital Vigente", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Titular: ${sesion.nombreUsuario}", fontWeight = FontWeight.SemiBold)
                    Text("Correo: ${sesion.correoUsuario}")
                    Text("Acreditación: ${sesion.rolUsuario} • OS-10 Activo")
                    Text("Sector autorizado: ${sesion.zonaAsignada}")
                    Text("Código ID: MG-2026-8492")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("✓ Documento verificado y habilitado para turno.", color = verde, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            },
            confirmButton = {
                Button(onClick = { mostrarCredencialDialog = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    // 4. Diálogo Cambiar Contraseña
    if (mostrarCambiarClaveDialog) {
        AlertDialog(
            onDismissRequest = {
                mostrarCambiarClaveDialog = false
                errorClave = null
            },
            title = { Text("Cambiar contraseña", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = claveActual,
                        onValueChange = { claveActual = it; errorClave = null },
                        label = { Text("Contraseña actual") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = claveNueva,
                        onValueChange = { claveNueva = it; errorClave = null },
                        label = { Text("Nueva contraseña (mín. 6 caracteres)") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = claveConfirmar,
                        onValueChange = { claveConfirmar = it; errorClave = null },
                        label = { Text("Confirmar nueva contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (errorClave != null) {
                        Text(errorClave!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    when {
                        claveActual.isBlank() || claveNueva.isBlank() || claveConfirmar.isBlank() -> {
                            errorClave = "Completa todos los campos"
                        }
                        claveNueva.length < 6 -> {
                            errorClave = "La nueva contraseña debe tener al menos 6 caracteres"
                        }
                        claveNueva != claveConfirmar -> {
                            errorClave = "Las contraseñas nuevas no coinciden"
                        }
                        else -> {
                            claveActual = ""
                            claveNueva = ""
                            claveConfirmar = ""
                            errorClave = null
                            mostrarCambiarClaveDialog = false
                            mensajeConfirmacion = "Contraseña actualizada con éxito"
                        }
                    }
                }) {
                    Text("Actualizar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarCambiarClaveDialog = false
                    errorClave = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 5. Diálogo Ayuda y Soporte
    if (mostrarAyudaDialog) {
        AlertDialog(
            onDismissRequest = { mostrarAyudaDialog = false },
            title = { Text("Ayuda y Soporte Técnico", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Central de Operaciones Modo Guardián", fontWeight = FontWeight.SemiBold)
                    Text("• Canal de Radio: Frecuencia 1 - Seguridad")
                    Text("• Mesa de Ayuda 24/7: +56 2 2800 4500")
                    Text("• Correo soporte: soporte@modoguardian.cl")
                }
            },
            confirmButton = {
                Button(onClick = {
                    mostrarAyudaDialog = false
                    mensajeConfirmacion = "Solicitud de asistencia enviada a Central"
                }) {
                    Text("Solicitar asistencia")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarAyudaDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // 6. Diálogo Términos y Políticas de Seguridad
    if (mostrarTerminosDialog) {
        AlertDialog(
            onDismissRequest = { mostrarTerminosDialog = false },
            title = { Text("Términos y Políticas de Seguridad", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("1. Uso exclusivo para personal autorizado en turno activo.")
                    Text("2. Todas las transmisiones de cámaras y confirmaciones de alerta quedan auditadas con sello horario.")
                    Text("3. Queda prohibida la divulgación externa de capturas o registros del sistema.")
                }
            },
            confirmButton = {
                Button(onClick = { mostrarTerminosDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Cabecera con botón directo para volver a Inicio
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateToInicio) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver a Inicio",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mi Perfil",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Cuenta y preferencias del turno • ${infoTurno.fechaHeader}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (mensajeConfirmacion != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = verde.copy(alpha = 0.14f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "✓ $mensajeConfirmacion",
                        color = verde,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "OK",
                        color = verde,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { mensajeConfirmacion = null }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tarjeta de identidad del usuario activo
        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = sesion.inicialesUsuario,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (sesion.nombreUsuario.equals(sesion.rolUsuario, ignoreCase = true)) {
                                sesion.nombreUsuario
                            } else {
                                "${sesion.nombreUsuario} — ${sesion.rolUsuario}"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = sesion.correoUsuario,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = verde.copy(alpha = 0.14f)
                        ) {
                            Text(
                                text = "✓ Correo verificado",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = verde,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    IconButton(onClick = { editarPerfilDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar perfil",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(
                        onClick = { editarPerfilDialog = true },
                        label = { Text("${sesion.rolUsuario} • ${sesion.zonaAsignada}", fontSize = 11.sp) }
                    )
                    AssistChip(
                        onClick = { mostrarTurnoDialog = true },
                        label = { Text("● Turno Activo", fontSize = 11.sp, color = verde) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Métricas sincronizadas con el turno real del usuario
        OutlinedCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { mostrarTurnoDialog = true }
                ) {
                    Text(infoTurno.horarioTurno, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Turno", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateToCamaras() }
                ) {
                    Text("${sesion.camarasAsignadas} asignadas", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Cámaras", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onNavigateToEventos() }
                ) {
                    Text("${sesion.alertasResueltasHoy} resueltas", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Alertas hoy", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "MI CUENTA",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedCard(shape = RoundedCornerShape(12.dp)) {
            Column {
                FilaOpcionPerfil(
                    icono = Icons.Default.Person,
                    titulo = "Mis cámaras asignadas",
                    badge = sesion.camarasAsignadas.toString(),
                    onClick = onNavigateToCamaras
                )
                HorizontalDivider()
                FilaOpcionPerfil(
                    icono = Icons.Default.Info,
                    titulo = "Historial de turnos",
                    subtitulo = "Registros de ${sesion.nombreUsuario}",
                    onClick = onNavigateToEventos
                )
                HorizontalDivider()
                FilaOpcionPerfil(
                    icono = Icons.Default.CheckCircle,
                    titulo = "Certificado de guardia / Credencial",
                    subtitulo = "Documento vigente • ${sesion.zonaAsignada}",
                    onClick = { mostrarCredencialDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SEGURIDAD",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedCard(shape = RoundedCornerShape(12.dp)) {
            Column {
                FilaOpcionPerfil(
                    icono = Icons.Default.Lock,
                    titulo = "Cambiar contraseña",
                    onClick = { mostrarCambiarClaveDialog = true }
                )
                HorizontalDivider()
                FilaSwitchPerfil(
                    icono = Icons.Default.Lock,
                    titulo = "Verificación en 2 pasos",
                    subtitulo = if (sesion.verificacion2Pasos) "Protección adicional activada" else "Protección desactivada",
                    checked = sesion.verificacion2Pasos,
                    onCheckedChange = {
                        sesion.verificacion2Pasos = it
                        mensajeConfirmacion = if (it) "Verificación en 2 pasos activada" else "Verificación en 2 pasos desactivada"
                    }
                )
                HorizontalDivider()
                FilaSwitchPerfil(
                    icono = Icons.Default.Notifications,
                    titulo = "Notificaciones Push",
                    subtitulo = if (sesion.notificacionesPush) "Avisos activados" else "Avisos silenciados",
                    checked = sesion.notificacionesPush,
                    onCheckedChange = {
                        sesion.notificacionesPush = it
                        mensajeConfirmacion = if (it) "Notificaciones Push activadas" else "Notificaciones Push silenciadas"
                    }
                )
                HorizontalDivider()
                FilaSwitchPerfil(
                    icono = Icons.Default.Notifications,
                    titulo = "Alertas críticas",
                    subtitulo = if (sesion.alertasCriticas) "Siempre visibles y sonoras" else "Solo aviso en pantalla",
                    checked = sesion.alertasCriticas,
                    onCheckedChange = {
                        sesion.alertasCriticas = it
                        mensajeConfirmacion = if (it) "Alertas críticas sonoras activadas" else "Alertas críticas en modo silencioso"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "APP",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedCard(shape = RoundedCornerShape(12.dp)) {
            Column {
                FilaSwitchPerfil(
                    icono = Icons.Default.Settings,
                    titulo = "Modo oscuro",
                    subtitulo = if (sesion.modoOscuro) "Tema oscuro aplicado" else "Tema claro aplicado",
                    checked = sesion.modoOscuro,
                    onCheckedChange = { sesion.modoOscuro = it }
                )
                HorizontalDivider()
                FilaOpcionPerfil(
                    icono = Icons.Default.Info,
                    titulo = "Ayuda y soporte",
                    subtitulo = "Guías y contacto",
                    onClick = { mostrarAyudaDialog = true }
                )
                HorizontalDivider()
                FilaOpcionPerfil(
                    icono = Icons.Default.Info,
                    titulo = "Términos y políticas de seguridad",
                    onClick = { mostrarTerminosDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCerrarSesion,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = rojo),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = rojo)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión", fontWeight = FontWeight.Bold, color = rojo)
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}

@Composable
private fun FilaOpcionPerfil(
    icono: ImageVector,
    titulo: String,
    subtitulo: String? = null,
    badge: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            if (subtitulo != null) {
                Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (badge != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = badge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FilaSwitchPerfil(
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}