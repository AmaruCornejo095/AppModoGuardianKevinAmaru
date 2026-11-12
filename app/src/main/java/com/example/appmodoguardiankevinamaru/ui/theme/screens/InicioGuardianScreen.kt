package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CamaraResumen(
    val id: String,
    val nombre: String,
    val zona: String,
    val hora: String,
    val enLinea: Boolean
)

@Composable
fun InicioGuardianScreen(
    sesion: SesionGuardianState,
    onNavigateToCamaras: () -> Unit = {},
    onNavigateToEventos: () -> Unit = {},
    onNavigateToAlertas: () -> Unit = {}
) {
    val infoTurno by rememberRelojTurnoActual()
    var mostrarDialogoIncidencia by remember { mutableStateOf(false) }
    var textoIncidencia by remember { mutableStateOf("") }

    val verdeEstado = Color(0xFF1E8E3E)
    val rojoAlerta = Color(0xFFD93025)
    val ambarAviso = Color(0xFFF9AB00)

    val camaras = remember(sesion.zonaAsignada, infoTurno.horaActual) {
        listOf(
            CamaraResumen("CAM-01", "Entrada Principal", sesion.zonaAsignada, infoTurno.horaActual, true),
            CamaraResumen("CAM-02", "Perímetro Norte", sesion.zonaAsignada, infoTurno.horaActual, true),
            CamaraResumen("CAM-03", "Bodega A", sesion.zonaAsignada, "--:--", false),
            CamaraResumen("CAM-04", "Estacionamiento", sesion.zonaAsignada, infoTurno.horaActual, true)
        )
    }

    val cantidadAlertas = if (sesion.alertaActiva != null) 1 else 0

    if (mostrarDialogoIncidencia) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoIncidencia = false },
            title = { Text("Reportar Incidencia", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Guardián: ${sesion.nombreUsuario} • ${sesion.zonaAsignada}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Describe la novedad detectada en tu turno:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = textoIncidencia,
                        onValueChange = { textoIncidencia = it },
                        placeholder = { Text("Ej. Puerta lateral sin seguro...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sesion.registrarIncidenciaManual(textoIncidencia)
                        textoIncidencia = ""
                        mostrarDialogoIncidencia = false
                        onNavigateToAlertas()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Enviar reporte")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoIncidencia = false }) {
                    Text("Cancelar")
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
        // Cabecera personalizada con el usuario activo
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sesion.inicialesUsuario,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hola, ${sesion.nombreUsuario}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Turno Activo • ${sesion.zonaAsignada}",
                    style = MaterialTheme.typography.bodySmall,
                    color = verdeEstado,
                    fontWeight = FontWeight.Medium
                )
            }
            Box(
                modifier = Modifier
                    .clickable { onNavigateToAlertas() }
                    .padding(end = 10.dp)
            ) {
                if (cantidadAlertas > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = rojoAlerta) {
                                Text(cantidadAlertas.toString(), color = Color.White, fontSize = 10.sp)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Sin alertas",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = verdeEstado.copy(alpha = 0.12f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = verdeEstado,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "EN LÍNEA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = verdeEstado
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjeta Estado General
        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado General",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "MODO GUARDIÁN • TURNO ${infoTurno.horarioTurno}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricaEstadoCard(
                        titulo = "TOTAL",
                        valor = sesion.camarasAsignadas.toString(),
                        subtitulo = "Cámaras",
                        colorAcento = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    MetricaEstadoCard(
                        titulo = "ACTIVAS",
                        valor = (sesion.camarasAsignadas - 1).toString(),
                        subtitulo = "En línea",
                        colorAcento = verdeEstado,
                        modifier = Modifier.weight(1f)
                    )
                    MetricaEstadoCard(
                        titulo = "ATENCIÓN",
                        valor = "1",
                        subtitulo = "Mantención",
                        colorAcento = ambarAviso,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "CAPACIDAD OPERATIVA",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "91%",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.91f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Última sincronización: ${infoTurno.horaActual}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "● Sincronizado",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = verdeEstado
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Cámaras Asignadas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cámaras Asignadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onNavigateToCamaras) {
                Text("Ver todas")
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            camaras.chunked(2).forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    fila.forEach { cam ->
                        OutlinedCard(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigateToCamaras() }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (cam.enLinea) rojoAlerta.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = if (cam.enLinea) "● REC" else "OFFLINE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (cam.enLinea) rojoAlerta else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "HD",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = cam.hora,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cam.nombre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = cam.zona,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = if (cam.enLinea) "En línea" else "Sin conexión",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium,
                                        color = if (cam.enLinea) verdeEstado else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Eventos en vivo del usuario
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Eventos en vivo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "● LIVE FEED",
                style = MaterialTheme.typography.labelSmall,
                color = rojoAlerta,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            sesion.eventosTurno.take(4).forEach { evento ->
                OutlinedCard(
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToEventos() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (evento.categoria == "Accesos") Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = evento.colorEstado,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = evento.colorEstado.copy(alpha = 0.14f)
                                ) {
                                    Text(
                                        text = evento.categoria.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = evento.colorEstado,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = evento.camara,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = evento.titulo,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = evento.detalle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        TextButton(
            onClick = onNavigateToEventos,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Ver historial completo")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { mostrarDialogoIncidencia = true },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Info, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reportar Incidencia", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun MetricaEstadoCard(
    titulo: String,
    valor: String,
    subtitulo: String,
    colorAcento: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = titulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colorAcento
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}