package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Dona con el total al centro y leyenda a la derecha (etiqueta ... valor)
@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier, tamano: Dp = 110.dp) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val vacio = MaterialTheme.colorScheme.outlineVariant
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(tamano)) {
                val grosor = size.minDimension * 0.2f
                val estilo = Stroke(width = grosor)
                val arcSize = Size(size.width - grosor, size.height - grosor)
                val topLeft = Offset(grosor / 2, grosor / 2)
                if (total <= 0f) {
                    drawArc(vacio, 0f, 360f, false, topLeft, arcSize, style = estilo)
                } else {
                    var inicio = -90f
                    datos.forEach { d ->
                        val barrido = d.valor / total * 360f
                        drawArc(d.color, inicio, barrido, false, topLeft, arcSize, style = estilo)
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(total.toInt().toString(), style = MaterialTheme.typography.titleLarge)
                Text("citas", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(20.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(d.color, RoundedCornerShape(3.dp)))
                    Spacer(Modifier.width(8.dp))
                    Text(d.etiqueta, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Text(d.valor.toInt().toString(), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
