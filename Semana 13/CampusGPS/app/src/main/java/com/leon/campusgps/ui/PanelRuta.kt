// Panel flotante de la ruta: destino, distancia, minutos a pie y pasos giro a giro.
package com.leon.campusgps.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.RoundaboutRight
import androidx.compose.material.icons.filled.Straight
import androidx.compose.material.icons.filled.UTurnLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.leon.campusgps.model.LugarConDistancia
import com.leon.campusgps.model.PasoRuta
import com.leon.campusgps.model.RutaUi
import com.leon.campusgps.util.formatearDistancia
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun PanelRuta(lugar: LugarConDistancia, ruta: RutaUi, onCerrar: () -> Unit) {
    val pasos = (ruta as? RutaUi.Lista)?.ruta?.pasos.orEmpty()
    var abierto by remember { mutableStateOf(false) }
    // La flecha gira al abrir o cerrar la lista de pasos
    val giro by animateFloatAsState(if (abierto) 0f else 180f, label = "flecha")

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
        shadowElevation = 4.dp,
        modifier = Modifier.animateContentSize()
    ) {
        Column {
            // Los pasos se despliegan hacia arriba, encima del resumen
            AnimatedVisibility(
                visible = abierto && pasos.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 220.dp)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pasos.forEach { FilaPaso(it) }
                        FilaPaso(PasoRuta("Llegas a ${lugar.lugar.nombre}", "LLEGADA", 0))
                    }
                    HorizontalDivider()
                }
            }

            Row(
                modifier = Modifier
                    .clickable(enabled = pasos.isNotEmpty()) { abierto = !abierto }
                    .padding(start = 12.dp, end = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ruta is RutaUi.Cargando) {
                    CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                } else {
                    Icon(
                        Icons.AutoMirrored.Filled.DirectionsWalk,
                        contentDescription = null,
                        tint = ColorRuta,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    resumen(lugar, ruta),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                // Botón para ver u ocultar los pasos (solo si la ruta va por las calles)
                if (pasos.isNotEmpty()) {
                    Icon(
                        Icons.Filled.ExpandLess,
                        contentDescription = if (abierto) "Ocultar pasos" else "Ver pasos",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(22.dp)
                            .rotate(giro)
                    )
                }
                IconButton(onClick = onCerrar, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Filled.Close, contentDescription = "Quitar ruta", modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

// Texto corto del panel según el estado de la ruta
private fun resumen(lugar: LugarConDistancia, ruta: RutaUi): String = when (ruta) {
    is RutaUi.Cargando -> "Calculando ruta a ${lugar.lugar.nombre}…"
    is RutaUi.Lista -> {
        val minutos = max(1, (ruta.ruta.segundos / 60f).roundToInt())
        "${lugar.lugar.nombre} · ${formatearDistancia(ruta.ruta.metros.toFloat())} · $minutos min"
    }
    // Sin servicio de rutas: distancia en línea recta
    else -> lugar.metros?.let { "${lugar.lugar.nombre} · ${formatearDistancia(it)} en línea recta" }
        ?: lugar.lugar.nombre
}

@Composable
private fun FilaPaso(paso: PasoRuta) {
    val llegada = paso.maniobra == "LLEGADA"
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    if (llegada) Color(0xFF2E7D32) else ColorRuta.copy(alpha = 0.12f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                iconoManiobra(paso.maniobra),
                contentDescription = null,
                tint = if (llegada) Color.White else ColorRuta,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(paso.instruccion, style = MaterialTheme.typography.bodyMedium)
            if (paso.metros > 0) {
                Text(
                    formatearDistancia(paso.metros.toFloat()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Icono de cada maniobra que devuelve la Routes API (TURN_LEFT, ROUNDABOUT_RIGHT, ...)
private fun iconoManiobra(maniobra: String): ImageVector = when {
    maniobra == "LLEGADA" -> Icons.Filled.Flag
    maniobra == "DEPART" -> Icons.Filled.NearMe
    maniobra.startsWith("UTURN") -> Icons.Filled.UTurnLeft
    maniobra.startsWith("ROUNDABOUT") -> Icons.Filled.RoundaboutRight
    maniobra.contains("LEFT") -> Icons.Filled.TurnLeft
    maniobra.contains("RIGHT") -> Icons.Filled.TurnRight
    else -> Icons.Filled.Straight
}
