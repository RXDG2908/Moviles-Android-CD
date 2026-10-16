package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

// Un punto de la línea de tiempo del historial clínico
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
    val linea = MaterialTheme.colorScheme.outlineVariant
    Row(Modifier.height(IntrinsicSize.Min)) {
        Canvas(Modifier.width(24.dp).fillMaxHeight()) {
            val x = size.width / 2
            if (!esUltimo) drawLine(linea, Offset(x, 14.dp.toPx()), Offset(x, size.height), strokeWidth = 2.dp.toPx())
            drawCircle(primario, 6.dp.toPx(), Offset(x, 10.dp.toPx()))
        }
        Column(Modifier.padding(start = 8.dp, bottom = 16.dp)) {
            Text(fecha, style = MaterialTheme.typography.labelMedium, color = primario)
            Text(servicio, style = MaterialTheme.typography.titleMedium)
            diagnostico?.let { Text("Diagnóstico: $it", style = MaterialTheme.typography.bodyMedium) }
            tratamiento?.takeIf { it.isNotBlank() }?.let { Text("Tratamiento: $it", style = MaterialTheme.typography.bodyMedium) }
            peso?.let { Text("Peso: $it kg", style = MaterialTheme.typography.bodyMedium) }
            Spacer(Modifier.height(4.dp))
        }
    }
}
