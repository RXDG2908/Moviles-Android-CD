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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier, tamano: Dp = 140.dp) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val vacio = MaterialTheme.colorScheme.outlineVariant
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(Modifier.size(tamano)) {
                val grosor = size.minDimension * 0.18f
                val estilo = Stroke(width = grosor)
                val inset = grosor / 2
                val arcSize = androidx.compose.ui.geometry.Size(size.width - grosor, size.height - grosor)
                val topLeft = androidx.compose.ui.geometry.Offset(inset, inset)
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
            Text(total.toInt().toString(), style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(d.color, CircleShape))
                    Spacer(Modifier.width(6.dp))
                    Text("${d.etiqueta}: ${d.valor.toInt()}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
