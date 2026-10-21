package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.theme.EstadoCancelada

// Un punto de la línea de tiempo del historial clínico: check + tarjeta con la atención
@Composable
fun HistorialItem(
    fecha: String,
    servicio: String,
    diagnostico: String?,
    tratamiento: String?,
    peso: Double?,
    esUltimo: Boolean = false
) {
    val primario = MaterialTheme.colorScheme.primary
    Row(Modifier.height(IntrinsicSize.Min)) {
        // Línea de tiempo
        Column(Modifier.width(28.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(22.dp).background(primario, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(14.dp))
            }
            if (!esUltimo) {
                Box(Modifier.width(2.dp).weight(1f).background(MaterialTheme.colorScheme.primaryContainer))
            }
        }
        Spacer(Modifier.width(10.dp))
        // Tarjeta de la atención
        Tarjeta(Modifier.weight(1f).padding(bottom = 12.dp)) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row {
                    Text(servicio, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                    Text(fecha, style = MaterialTheme.typography.labelMedium, color = primario)
                }
                diagnostico?.let { TextoGris(it) }
                tratamiento?.takeIf { it.isNotBlank() }?.let { TextoGris("Tratamiento: $it") }
                peso?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Favorite, contentDescription = null, tint = EstadoCancelada, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Peso: $it kg", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
