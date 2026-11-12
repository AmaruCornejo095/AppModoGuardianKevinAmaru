package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CamarasScreen(sesion: SesionGuardianState) {
    val infoTurno by rememberRelojTurnoActual()

    val listaCamaras = listOf(
        "Entrada Principal" to "CAM-01",
        "Perímetro Norte" to "CAM-02",
        "Bodega A" to "CAM-03",
        "Estacionamiento" to "CAM-04"
    )

    var indiceCamara by remember { mutableIntStateOf(0) }
    var grabando by remember { mutableStateOf(true) }
    var microfonoActivo by remember { mutableStateOf(false) }
    var luzActiva by remember { mutableStateOf(false) }
    var pantallaExpandida by remember { mutableStateOf(false) }
    var mensajeAccion by remember { mutableStateOf<String?>(null) }

    val (nombreCamara, codigoCamara) = listaCamaras[indiceCamara]
    val rojoRec = Color(0xFFD93025)
    val verdeOnline = Color(0xFF1E8E3E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = nombreCamara,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "● EN VIVO • HD • ${sesion.zonaAsignada}",
                    style = MaterialTheme.typography.labelSmall,
                    color = rojoRec,
                    fontWeight = FontWeight.Bold
                )
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (grabando) rojoRec.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = if (grabando) "● REC" else "PAUSADA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (grabando) rojoRec else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listaCamaras.forEachIndexed { index, (_, codigo) ->
                FilterChip(
                    selected = indiceCamara == index,
                    onClick = {
                        indiceCamara = index
                        mensajeAccion = null
                    },
                    label = { Text(codigo, fontSize = 11.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (pantallaExpandida) 340.dp else 250.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF142420))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = infoTurno.fechaCompleta,
                        color = Color(0xFF81C995),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "MODO GUARDIÁN $codigoCamara • ${sesion.nombreUsuario}",
                        color = Color(0xFF81C995),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF81C995).copy(alpha = 0.16f)
                ) {
                    Text(
                        text = if (luzActiva) "LUZ • ON" else "IR • AUTO",
                        color = Color(0xFF81C995),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 2.dp.toPx()
                val len = 18.dp.toPx()
                val colorGuia = Color(0xFF81C995).copy(alpha = 0.55f)
                val topY = 38.dp.toPx()
                val bottomY = size.height - 34.dp.toPx()

                drawLine(colorGuia, Offset(0f, topY), Offset(len, topY), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(0f, topY), Offset(0f, topY + len), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(size.width - len, topY), Offset(size.width, topY), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(size.width, topY), Offset(size.width, topY + len), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(0f, bottomY), Offset(len, bottomY), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(0f, bottomY - len), Offset(0f, bottomY), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(size.width - len, bottomY), Offset(size.width, bottomY), stroke, StrokeCap.Round)
                drawLine(colorGuia, Offset(size.width, bottomY - len), Offset(size.width, bottomY), stroke, StrokeCap.Round)
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.55f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (grabando) "● REC  00:14:37" else "■ DETENIDO",
                        color = if (grabando) Color(0xFFF28B82) else Color.LightGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "H.265 • 25FPS",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (mensajeAccion != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensajeAccion!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ControlCamaraBoton(
                etiqueta = "FOTO",
                icono = Icons.Default.Search,
                activo = false,
                onClick = { mensajeAccion = "Captura guardada desde $codigoCamara" }
            )
            ControlCamaraBoton(
                etiqueta = if (grabando) "PARAR" else "GRABAR",
                icono = Icons.Default.Refresh,
                activo = grabando,
                colorActivo = rojoRec,
                onClick = {
                    grabando = !grabando
                    mensajeAccion = if (grabando) "Grabación iniciada" else "Grabación detenida"
                }
            )
            ControlCamaraBoton(
                etiqueta = "HABLAR",
                icono = Icons.Default.Phone,
                activo = microfonoActivo,
                onClick = {
                    microfonoActivo = !microfonoActivo
                    mensajeAccion = if (microfonoActivo) "Audio bidireccional activo" else "Micrófono silenciado"
                }
            )
            ControlCamaraBoton(
                etiqueta = "LUZ",
                icono = Icons.Default.Info,
                activo = luzActiva,
                onClick = {
                    luzActiva = !luzActiva
                    mensajeAccion = if (luzActiva) "Reflector encendido" else "Modo infrarrojo automático"
                }
            )
            ControlCamaraBoton(
                etiqueta = "FULL",
                icono = Icons.Default.CheckCircle,
                activo = pantallaExpandida,
                onClick = { pantallaExpandida = !pantallaExpandida }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ESTADO DEL DISPOSITIVO",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DispositivoInfoItem(
                        titulo = "ESTADO",
                        valor = "En línea",
                        colorValor = verdeOnline,
                        modifier = Modifier.weight(1f)
                    )
                    DispositivoInfoItem(
                        titulo = "SEÑAL",
                        valor = "Excelente",
                        colorValor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    DispositivoInfoItem(
                        titulo = "GRABACIÓN",
                        valor = "Nube + Local",
                        colorValor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlCamaraBoton(
    etiqueta: String,
    icono: ImageVector,
    activo: Boolean,
    colorActivo: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = if (activo) colorActivo else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(52.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = etiqueta,
                    tint = if (activo) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = etiqueta,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DispositivoInfoItem(
    titulo: String,
    valor: String,
    colorValor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = titulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = colorValor
            )
        }
    }
}