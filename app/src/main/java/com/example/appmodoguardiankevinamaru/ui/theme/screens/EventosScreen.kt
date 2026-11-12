package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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

@Composable
fun EventosScreen(sesion: SesionGuardianState) {
    val infoTurno by rememberRelojTurnoActual()

    var tabSeleccionada by remember { mutableIntStateOf(0) }
    val tabs = listOf("Todos", "Alertas", "Eventos", "Accesos")
    var busqueda by remember { mutableStateOf("") }
    var filtroChip by remember { mutableStateOf("Hoy") }
    var reporteExportado by remember { mutableStateOf(false) }

    val rojo = Color(0xFFD93025)
    val ambar = Color(0xFFF9AB00)

    val eventos = sesion.eventosTurno
    val totalEventos = eventos.size
    val totalCriticas = sesion.alertasResueltasHoy
    val totalPendientes = if (sesion.alertaActiva != null) 1 else 0

    val eventosFiltrados = eventos.filter { item ->
        val coincideTab = tabs[tabSeleccionada] == "Todos" || item.categoria == tabs[tabSeleccionada]
        val coincideBusqueda = busqueda.isBlank() ||
                item.titulo.contains(busqueda, ignoreCase = true) ||
                item.ubicacion.contains(busqueda, ignoreCase = true) ||
                item.camara.contains(busqueda, ignoreCase = true)
        val coincideChip = when (filtroChip) {
            "Críticas" -> item.colorEstado == rojo || item.estadoTexto.contains("Pendiente", ignoreCase = true)
            "Perímetro" -> item.ubicacion.contains("Perímetro", ignoreCase = true) || item.ubicacion.contains(sesion.zonaAsignada, ignoreCase = true)
            "Cámara 01" -> item.camara == "CAM-01"
            else -> true
        }
        coincideTab && coincideBusqueda && coincideChip
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Historial",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "● Turno actual • ${infoTurno.fechaHeader} • ${infoTurno.horarioTurno} (${infoTurno.horaActual})",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF1E8E3E),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Operador: ${sesion.nombreUsuario} • ${sesion.zonaAsignada}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Resumen del turno sincronizado con el perfil y reloj real
        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("HOY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("$totalEventos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("eventos registrados", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column {
                        Text("CRÍTICAS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = rojo)
                        Text("$totalCriticas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("alertas resueltas", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column {
                        Text("PENDIENTE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ambar)
                        Text("$totalPendientes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("requiere atención", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "PROGRESO DE TURNO",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${infoTurno.porcentajeTexto} • ${infoTurno.tiempoRestanteTexto}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { infoTurno.progreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        TabRow(selectedTabIndex = tabSeleccionada) {
            tabs.forEachIndexed { index, titulo ->
                Tab(
                    selected = tabSeleccionada == index,
                    onClick = { tabSeleccionada = index },
                    text = { Text(titulo, fontSize = 13.sp, fontWeight = FontWeight.SemiBold) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar por cámara, zona o tipo...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Hoy", "Cámara 01", "Perímetro", "Críticas").forEach { chip ->
                FilterChip(
                    selected = filtroChip == chip,
                    onClick = { filtroChip = chip },
                    label = { Text(chip, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val agrupados = eventosFiltrados.groupBy { it.franja }
        agrupados.forEach { (franja, lista) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = franja,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${lista.size} eventos",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                lista.forEach { evento ->
                    OutlinedCard(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
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
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = evento.titulo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = evento.ubicacion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = evento.detalle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = evento.colorEstado.copy(alpha = 0.14f)
                                ) {
                                    Text(
                                        text = evento.estadoTexto,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = evento.colorEstado,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = evento.camara,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { reporteExportado = true },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (reporteExportado) "Reporte PDF de ${sesion.nombreUsuario} exportado" else "Exportar reporte de turno (PDF)",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}