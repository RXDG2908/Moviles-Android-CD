package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Línea con un punto por valor; debajo, la primera y la última etiqueta
@Composable
fun GraficoLineas(
    valores: List<Float>,
    etiquetas: List<String>,
    color: Color,
    modifier: Modifier = Modifier,
    alto: Dp = 120.dp
) {
    val guia = MaterialTheme.colorScheme.outlineVariant
    val fondoPunto = MaterialTheme.colorScheme.surface
    Column(modifier) {
        Canvas(Modifier.fillMaxWidth().height(alto)) {
            if (valores.isEmpty()) return@Canvas
            val margen = 10.dp.toPx()
            val minimo = valores.min()
            val maximo = valores.max()
            val ancho = size.width - margen * 2
            val altoUtil = size.height - margen * 2

            // x: repartidos a lo ancho; y: el mayor arriba y el menor abajo
            val puntos = valores.mapIndexed { i, v ->
                val x = if (valores.size == 1) size.width / 2 else margen + ancho * i / (valores.size - 1)
                val y = if (maximo == minimo) size.height / 2 else margen + altoUtil * (1 - (v - minimo) / (maximo - minimo))
                Offset(x, y)
            }

            // Línea guía de abajo
            drawLine(guia, Offset(0f, size.height - 1), Offset(size.width, size.height - 1), strokeWidth = 2f)

            val camino = Path().apply {
                moveTo(puntos.first().x, puntos.first().y)
                puntos.drop(1).forEach { lineTo(it.x, it.y) }
            }
            drawPath(camino, color, style = Stroke(width = 3.dp.toPx()))
            puntos.forEach {
                drawCircle(color, radius = 5.dp.toPx(), center = it)
                drawCircle(fondoPunto, radius = 2.dp.toPx(), center = it)
            }
        }
        if (etiquetas.isNotEmpty()) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    etiquetas.first(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                if (etiquetas.size > 1) {
                    Text(
                        etiquetas.last(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
