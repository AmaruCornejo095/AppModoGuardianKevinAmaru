package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AlertasScreen(sesion: SesionGuardianState) {
    val alerta = sesion.alertaActiva
    var ultimoMensaje by remember { mutableStateOf<String?>(null) }
    var segundosRestantes by remember(alerta?.id) { mutableIntStateOf(106) }

    val rojoCritico = Color(0xFFD93025)
    val verdeIA = Color(0xFF1E8E3E)
    val ambar = Color(0xFFF9AB00)

    LaunchedEffect(alerta?.id) {
        while (alerta != null && segundosRestantes > 0) {
            delay(1000L)
            segundosRestantes -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Cabecera
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Centro de Alertas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            if (alerta != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = rojoCritico
                ) {
                    Text(
                        text = "● CRÍTICO",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = verdeIA.copy(alpha = 0.14f)
                ) {
                    Text(
                        text = "● SIN NOVEDADES",
                        color = verdeIA,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (ultimoMensaje != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = verdeIA.copy(alpha = 0.14f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = "✓ $ultimoMensaje",
                    color = verdeIA,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        if (alerta == null) {
            // Estado normal: NO se muestra "Acciones Requeridas" porque no ha ocurrido ningún evento
            ElevatedCard(
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = verdeIA.copy(alpha = 0.14f),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = verdeIA,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Sin alertas críticas activas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Todos los sensores y cámaras de ${sesion.zonaAsignada} operan con normalidad para ${sesion.nombreUsuario}. Las acciones requeridas aparecerán automáticamente si ocurre un evento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedButton(
                        onClick = {
                            ultimoMensaje = null
                            sesion.dispararAlertaCritica(
                                ubicacion = "${sesion.zonaAsignada} - Cámara 04",
                                zonaDetalle = "${sesion.zonaAsignada} - Sector B"
                            )
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Simular detección de evento")
                    }
                }
            }
        } else {
            // Ocurrió una alerta: se muestra el detalle completo y "ACCIONES REQUERIDAS"
            ElevatedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = rojoCritico.copy(alpha = 0.08f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = rojoCritico.copy(alpha = 0.16f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = rojoCritico
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = alerta.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = rojoCritico,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = alerta.ubicacion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = alerta.fechaHora,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Confianza IA: ${alerta.confianzaIA}% persona detectada",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = verdeIA
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { alerta.confianzaIA / 100f },
                        color = verdeIA,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Clip de video y mapa de perímetro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF1A2230),
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "● ${alerta.camara} • REC",
                            color = Color(0xFFF28B82),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PlayArrow, contentDescription = "Ver video", tint = Color.White)
                            }
                        }
                        Text(
                            text = "Ver video de 10s",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .weight(1f)
                        .height(130.dp)
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "NORTE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                            drawRect(
                                color = Color.Gray.copy(alpha = 0.5f),
                                topLeft = Offset(size.width * 0.2f, size.height * 0.25f),
                                size = androidx.compose.ui.geometry.Size(size.width * 0.6f, size.height * 0.5f),
                                style = Stroke(width = 2f, pathEffect = dash)
                            )
                            drawCircle(
                                color = rojoCritico,
                                radius = 10f,
                                center = Offset(size.width * 0.5f, size.height * 0.35f)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = rojoCritico,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = alerta.camara,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = sesion.zonaAsignada.uppercase(),
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Detalles del evento
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "DETALLES DEL EVENTO",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FilaDetalleEvento("Tipo", alerta.tipo)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalleEvento("Zona", alerta.zonaDetalle)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalleEvento("Cámara", alerta.camara)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FilaDetalleEvento("Duración", alerta.duracion)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ACCIONES REQUERIDAS (solo visible cuando existe una alerta activa)
            Text(
                text = "ACCIONES REQUERIDAS",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = rojoCritico
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    sesion.resolverAlertaActiva("Confirmada y enviada a Central", esFalsaAlarma = false)
                    ultimoMensaje = "Alerta confirmada por ${sesion.nombreUsuario} y enviada a Central"
                },
                colors = ButtonDefaults.buttonColors(containerColor = rojoCritico),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirmar Alerta - Enviar a Central", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    sesion.resolverAlertaActiva("Descartada como falsa alarma", esFalsaAlarma = true)
                    ultimoMensaje = "Evento marcado como Falsa Alarma por ${sesion.nombreUsuario}"
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = ambar)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Falsa Alarma", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    sesion.resolverAlertaActiva("Incidencia reportada con evidencia fotográfica", esFalsaAlarma = false)
                    ultimoMensaje = "Incidencia con evidencia guardada en el historial de ${sesion.nombreUsuario}"
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Text("Reportar Incidencia con foto", fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    ultimoMensaje = "Llamando a Central de Operaciones (${sesion.zonaAsignada})..."
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Llamar a Central", fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(12.dp))

            val minRest = segundosRestantes / 60
            val segRest = segundosRestantes % 60
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "● TIEMPO DE RESPUESTA",
                    style = MaterialTheme.typography.labelSmall,
                    color = ambar,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = String.format("%02d:%02d restante", minRest, segRest),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (segundosRestantes / 120f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FilaDetalleEvento(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}