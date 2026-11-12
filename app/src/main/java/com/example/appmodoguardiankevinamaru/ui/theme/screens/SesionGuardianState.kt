package com.example.appmodoguardiankevinamaru.ui.theme.screens

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class AlertaCriticaItem(
    val id: String,
    val titulo: String,
    val ubicacion: String,
    val zonaDetalle: String,
    val camara: String,
    val fechaHora: String,
    val confianzaIA: Int,
    val tipo: String,
    val duracion: String
)

data class EventoHistorial(
    val franja: String,
    val titulo: String,
    val ubicacion: String,
    val detalle: String,
    val camara: String,
    val estadoTexto: String,
    val categoria: String,
    val colorEstado: Color
)

data class InfoTurnoActual(
    val horarioTurno: String,
    val fechaHeader: String,
    val fechaCompleta: String,
    val horaActual: String,
    val progreso: Float,
    val porcentajeTexto: String,
    val tiempoRestanteTexto: String
)

class SesionGuardianState {
    var correoUsuario by mutableStateOf("usuario@modoguardian.cl")
    var nombreUsuario by mutableStateOf("Usuario")
    var rolUsuario by mutableStateOf("Guardián")
    var zonaAsignada by mutableStateOf("Zona Sur")
    var camarasAsignadas by mutableIntStateOf(12)
    var alertasResueltasHoy by mutableIntStateOf(2)

    // Preferencias globales de Perfil y Tema
    var modoOscuro by mutableStateOf(false)
    var verificacion2Pasos by mutableStateOf(true)
    var notificacionesPush by mutableStateOf(true)
    var alertasCriticas by mutableStateOf(true)

    var alertaActiva by mutableStateOf<AlertaCriticaItem?>(null)
    val eventosTurno = mutableStateListOf<EventoHistorial>()

    val inicialesUsuario: String
        get() {
            if (nombreUsuario.equals("Usuario", ignoreCase = true)) return "US"
            val partes = nombreUsuario.trim()
                .replace(".", "")
                .split(" ")
                .filter { it.isNotBlank() }
            return when {
                partes.size >= 2 -> "${partes[0].first().uppercaseChar()}${partes[1].first().uppercaseChar()}"
                partes.size == 1 && partes[0].length >= 2 -> partes[0].take(2).uppercase()
                partes.size == 1 -> partes[0].uppercase()
                else -> "US"
            }
        }

    fun iniciarSesionConUsuario(correo: String, nombreRegistrado: String? = null) {
        val correoLimpio = correo.trim().ifBlank { "usuario@modoguardian.cl" }
        correoUsuario = correoLimpio

        nombreUsuario = if (!nombreRegistrado.isNullOrBlank() &&
            !nombreRegistrado.trim().equals("Usuario", ignoreCase = true)
        ) {
            nombreRegistrado.trim()
        } else {
            generarNombreRelacionado(correoLimpio)
        }

        alertaActiva = null
        alertasResueltasHoy = 2
        cargarEventosInicialesDelTurno()
    }

    fun cerrarSesion() {
        alertaActiva = null
        eventosTurno.clear()
    }

    fun dispararAlertaCritica(
        titulo: String = "Movimiento no autorizado detectado",
        ubicacion: String = "Perímetro Norte - Cámara 04",
        zonaDetalle: String = "Perímetro Norte - Sector B",
        camara: String = "CAM-04",
        tipo: String = "Intrusión perimetral"
    ) {
        val ahora = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.forLanguageTag("es-CL")).format(Date())
        alertaActiva = AlertaCriticaItem(
            id = "ALT-${System.currentTimeMillis() % 10000}",
            titulo = titulo,
            ubicacion = ubicacion,
            zonaDetalle = zonaDetalle,
            camara = camara,
            fechaHora = ahora,
            confianzaIA = 92,
            tipo = tipo,
            duracion = "12 segundos"
        )
    }

    fun resolverAlertaActiva(accion: String, esFalsaAlarma: Boolean = false) {
        val alerta = alertaActiva ?: return
        val cal = Calendar.getInstance()
        val horaActual = SimpleDateFormat("HH:mm", Locale.forLanguageTag("es-CL")).format(cal.time)
        val franjaActual = obtenerFranjaHoraria(cal.get(Calendar.HOUR_OF_DAY))

        eventosTurno.add(
            0,
            EventoHistorial(
                franja = franjaActual,
                titulo = alerta.titulo,
                ubicacion = alerta.ubicacion,
                detalle = "$horaActual • ${alerta.zonaDetalle} • $accion",
                camara = alerta.camara,
                estadoTexto = if (esFalsaAlarma) "Falsa alarma" else "Confirmada por $nombreUsuario",
                categoria = "Alertas",
                colorEstado = if (esFalsaAlarma) Color.Gray else Color(0xFFD93025)
            )
        )
        alertasResueltasHoy += 1
        alertaActiva = null
    }

    fun registrarIncidenciaManual(descripcion: String) {
        val texto = descripcion.trim().ifBlank { "Incidencia reportada en ronda" }
        val cal = Calendar.getInstance()
        val horaActual = SimpleDateFormat("HH:mm", Locale.forLanguageTag("es-CL")).format(cal.time)
        val franjaActual = obtenerFranjaHoraria(cal.get(Calendar.HOUR_OF_DAY))

        eventosTurno.add(
            0,
            EventoHistorial(
                franja = franjaActual,
                titulo = texto,
                ubicacion = zonaAsignada,
                detalle = "$horaActual • Reportado por $nombreUsuario",
                camara = "CAM-01",
                estadoTexto = "Pendiente",
                categoria = "Alertas",
                colorEstado = Color(0xFFF9AB00)
            )
        )

        dispararAlertaCritica(
            titulo = texto,
            ubicacion = "$zonaAsignada - Cámara 01",
            zonaDetalle = zonaAsignada,
            camara = "CAM-01",
            tipo = "Reporte manual de usuario"
        )
    }

    private fun cargarEventosInicialesDelTurno() {
        eventosTurno.clear()
        val cal = Calendar.getInstance()
        val hora = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE)

        val franja1 = obtenerFranjaHoraria(hora)
        val franja2 = obtenerFranjaHoraria((hora - 1 + 24) % 24)
        val franja3 = obtenerFranjaHoraria((hora - 2 + 24) % 24)

        val h1 = String.format(Locale.US, "%02d:%02d", hora, (min - 5).coerceAtLeast(0))
        val h2 = String.format(Locale.US, "%02d:15", (hora - 1 + 24) % 24)
        val h3 = String.format(Locale.US, "%02d:42", (hora - 1 + 24) % 24)
        val h4 = String.format(Locale.US, "%02d:10", (hora - 2 + 24) % 24)

        eventosTurno.addAll(
            listOf(
                EventoHistorial(
                    franja = franja1,
                    titulo = "Inicio de monitoreo en turno",
                    ubicacion = zonaAsignada,
                    detalle = "$h1 • Sesión iniciada por $nombreUsuario",
                    camara = "CAM-01",
                    estadoTexto = "Autorizado",
                    categoria = "Accesos",
                    colorEstado = Color(0xFF1E8E3E)
                ),
                EventoHistorial(
                    franja = franja2,
                    titulo = "Ronda de perímetro completada",
                    ubicacion = zonaAsignada,
                    detalle = "$h3 • $nombreUsuario • 18 checkpoints verificados",
                    camara = "CAM-04",
                    estadoTexto = "Confirmada",
                    categoria = "Eventos",
                    colorEstado = Color(0xFF1E8E3E)
                ),
                EventoHistorial(
                    franja = franja2,
                    titulo = "Acceso autorizado en portón",
                    ubicacion = "Entrada Principal",
                    detalle = "$h2 • Credencial #4582 • Control de acceso",
                    camara = "CAM-01",
                    estadoTexto = "Autorizado",
                    categoria = "Accesos",
                    colorEstado = Color(0xFF1E8E3E)
                ),
                EventoHistorial(
                    franja = franja3,
                    titulo = "Verificación de enlace de cámaras",
                    ubicacion = "Bodega A",
                    detalle = "$h4 • Diagnóstico automático completado",
                    camara = "CAM-02",
                    estadoTexto = "Resuelto",
                    categoria = "Eventos",
                    colorEstado = Color(0xFF1A73E8)
                )
            )
        )
    }

    private fun obtenerFranjaHoraria(hora: Int): String {
        val siguiente = (hora + 1) % 24
        return String.format(Locale.US, "%02d:00 - %02d:00", hora, siguiente)
    }

    private fun generarNombreRelacionado(correo: String): String {
        val localeEs = Locale.forLanguageTag("es-CL")
        val prefijo = correo.substringBefore("@").replace(Regex("[0-9]"), "").trim()

        val genericos = setOf(
            "guardian", "admin", "usuario", "user", "operador",
            "test", "prueba", "demo", "invitado", "soporte", "info", "contacto"
        )

        if (prefijo.isBlank() || prefijo.lowercase(localeEs) in genericos) {
            return "Usuario"
        }

        val nombresPorPrefijo = mapOf(
            "ke" to "Kevin", "vi" to "Victor", "ca" to "Carlos", "ma" to "Matías",
            "se" to "Sebastián", "di" to "Diego", "ni" to "Nicolás", "fe" to "Felipe",
            "an" to "Andrés", "ju" to "Juan", "lu" to "Luis", "ro" to "Rodrigo",
            "cr" to "Cristian", "pa" to "Pablo", "go" to "Gonzalo", "ig" to "Ignacio",
            "ja" to "Javier", "da" to "Daniel", "al" to "Alejandro", "ed" to "Eduardo",
            "fr" to "Francisco", "ga" to "Gabriel", "he" to "Héctor", "jo" to "Jorge",
            "le" to "Leonardo", "mi" to "Miguel", "os" to "Óscar", "pe" to "Pedro",
            "ra" to "Rafael", "sa" to "Samuel", "to" to "Tomás", "va" to "Valentina",
            "so" to "Sofía", "co" to "Constanza", "is" to "Isidora", "fl" to "Florencia",
            "na" to "Natalia", "ka" to "Karina"
        )

        fun capitalizar(texto: String): String =
            texto.lowercase(localeEs).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(localeEs) else it.toString()
            }

        fun limpiarApellido(texto: String): String {
            val base = texto.lowercase(localeEs)
            val limpio = if (base.length >= 5 &&
                (base[base.length - 2] == 's' || base[base.length - 2] == 'z') &&
                base.last() !in listOf('a', 'e', 'i', 'o', 'u', 's', 'z')
            ) {
                base.dropLast(1)
            } else {
                base
            }
            return capitalizar(limpio)
        }

        val partes = prefijo.split(".", "_", "-").map { it.trim() }.filter { it.isNotBlank() }

        return when {
            partes.size >= 2 -> {
                val p1 = partes[0].lowercase(localeEs)
                val p2 = partes[1]
                val nombre = when {
                    p1.length <= 2 -> nombresPorPrefijo[p1] ?: "${p1.first().uppercaseChar()}."
                    else -> capitalizar(p1)
                }
                val apellido = if (p2.length <= 2) "${p2.first().uppercaseChar()}." else limpiarApellido(p2)
                "$nombre $apellido"
            }
            partes.size == 1 -> {
                val unica = partes[0].lowercase(localeEs)
                if (unica.length <= 2) {
                    nombresPorPrefijo[unica] ?: "Usuario"
                } else {
                    capitalizar(unica)
                }
            }
            else -> "Usuario"
        }
    }
}

@Composable
fun rememberRelojTurnoActual(): State<InfoTurnoActual> {
    return produceState(initialValue = calcularInfoTurnoActual()) {
        while (true) {
            value = calcularInfoTurnoActual()
            delay(1000L)
        }
    }
}

fun calcularInfoTurnoActual(): InfoTurnoActual {
    val cal = Calendar.getInstance()
    val ahora = cal.time
    val localeEs = Locale.forLanguageTag("es-CL")

    val hora = cal.get(Calendar.HOUR_OF_DAY)
    val minuto = cal.get(Calendar.MINUTE)

    val (horarioTurno, minutosTranscurridos) = when {
        hora in 6..13 -> "06:00 - 14:00" to ((hora - 6) * 60 + minuto)
        hora in 14..21 -> "14:00 - 22:00" to ((hora - 14) * 60 + minuto)
        hora >= 22 -> "22:00 - 06:00" to ((hora - 22) * 60 + minuto)
        else -> "22:00 - 06:00" to ((hora + 2) * 60 + minuto)
    }

    val duracionTurnoMin = 480
    val transcurridosClamped = minutosTranscurridos.coerceIn(0, duracionTurnoMin)
    val restantesMin = (duracionTurnoMin - transcurridosClamped).coerceAtLeast(0)
    val progreso = transcurridosClamped.toFloat() / duracionTurnoMin.toFloat()
    val porcentaje = (progreso * 100).toInt()

    val horasRestantes = restantesMin / 60
    val minsRestantes = restantesMin % 60

    val formatoDiaMes = SimpleDateFormat("dd MMM yyyy", localeEs)
    val formatoCompleto = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", localeEs)
    val formatoHora = SimpleDateFormat("HH:mm", localeEs)

    val fechaHeader = formatoDiaMes.format(ahora).replaceFirstChar { it.uppercase() }

    return InfoTurnoActual(
        horarioTurno = horarioTurno,
        fechaHeader = fechaHeader,
        fechaCompleta = formatoCompleto.format(ahora),
        horaActual = formatoHora.format(ahora),
        progreso = progreso,
        porcentajeTexto = "$porcentaje%",
        tiempoRestanteTexto = "${horasRestantes}h ${minsRestantes}m restantes"
    )
}

fun extraerCorreoDeSesion(obj: Any?, profundidad: Int = 0): String? {
    if (obj == null || profundidad > 3) return null
    if (obj is String && obj.contains("@")) return obj.trim()
    if (obj is StateFlow<*>) return extraerCorreoDeSesion(obj.value, profundidad + 1)
    if (obj is State<*>) return extraerCorreoDeSesion(obj.value, profundidad + 1)

    return try {
        for (field in obj.javaClass.declaredFields) {
            field.isAccessible = true
            val valor = field.get(obj) ?: continue
            if (valor is String && valor.contains("@")) {
                return valor.trim()
            }
            if (valor is StateFlow<*> || valor is State<*> ||
                field.name.contains("state", ignoreCase = true) ||
                field.name.contains("estado", ignoreCase = true)
            ) {
                val encontrado = extraerCorreoDeSesion(valor, profundidad + 1)
                if (encontrado != null) return encontrado
            }
        }
        null
    } catch (_: Exception) {
        null
    }
}