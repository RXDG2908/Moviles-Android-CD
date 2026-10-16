package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier, alto: Dp = 140.dp) {
    val maximo = (datos.maxOfOrNull { it.valor } ?: 0f).coerceAtLeast(1f)
    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(alto)) {
            if (datos.isEmpty()) return@Canvas
            val ancho = size.width / datos.size
            val barra = ancho * 0.55f
            datos.forEachIndexed { i, d ->
                val h = d.valor / maximo * size.height
                drawRoundRect(
                    color = d.color,
                    topLeft = Offset(i * ancho + (ancho - barra) / 2, size.height - h),
                    size = Size(barra, h),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Top) {
            datos.forEach { d ->
                Text(
                    "${d.etiqueta}\n${d.valor.toInt()}",
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
