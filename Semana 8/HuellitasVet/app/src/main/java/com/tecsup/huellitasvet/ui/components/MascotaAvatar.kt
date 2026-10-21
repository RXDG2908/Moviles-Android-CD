package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie

// Carita de perro, gato u otro animal dibujada con Canvas.
// El círculo de fondo usa el color del contenedor del rol (verde o azul claro).
@Composable
fun MascotaAvatar(especie: Especie, modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    val fondo = MaterialTheme.colorScheme.primaryContainer
    val cara = when (especie) {
        Especie.PERRO -> Color(0xFFD9A066)
        Especie.GATO -> Color(0xFF9CA3AF)
        Especie.OTRO -> Color(0xFFD6D3D1)
    }
    val oreja = when (especie) {
        Especie.PERRO -> Color(0xFF92400E)
        Especie.GATO -> Color(0xFF6B7280)
        Especie.OTRO -> Color(0xFFF9A8D4)
    }
    Canvas(modifier = modifier.size(tamano)) {
        val r = size.minDimension / 2
        val c = Offset(size.width / 2, size.height / 2)
        drawCircle(fondo, r, c)
        when (especie) {
            Especie.PERRO -> {
                // orejas caídas
                drawOval(oreja, Offset(c.x - r * 0.72f, c.y - r * 0.42f), Size(r * 0.42f, r * 0.75f))
                drawOval(oreja, Offset(c.x + r * 0.30f, c.y - r * 0.42f), Size(r * 0.42f, r * 0.75f))
            }
            Especie.GATO -> {
                // orejas en punta
                drawPath(triangulo(c.x - r * 0.58f, c.y - r * 0.05f, c.x - r * 0.48f, c.y - r * 0.78f, c.x - r * 0.08f, c.y - r * 0.38f), cara)
                drawPath(triangulo(c.x + r * 0.58f, c.y - r * 0.05f, c.x + r * 0.48f, c.y - r * 0.78f, c.x + r * 0.08f, c.y - r * 0.38f), cara)
            }
            Especie.OTRO -> {
                // orejas largas (conejo)
                drawOval(cara, Offset(c.x - r * 0.4f, c.y - r * 0.95f), Size(r * 0.28f, r * 0.75f))
                drawOval(cara, Offset(c.x + r * 0.12f, c.y - r * 0.95f), Size(r * 0.28f, r * 0.75f))
                drawOval(oreja, Offset(c.x - r * 0.33f, c.y - r * 0.85f), Size(r * 0.14f, r * 0.5f))
                drawOval(oreja, Offset(c.x + r * 0.19f, c.y - r * 0.85f), Size(r * 0.14f, r * 0.5f))
            }
        }
        drawCircle(cara, r * 0.52f, Offset(c.x, c.y + r * 0.1f))
        dibujarRasgos(c, r)
    }
}

private fun triangulo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) = Path().apply {
    moveTo(x1, y1); lineTo(x2, y2); lineTo(x3, y3); close()
}

private fun DrawScope.dibujarRasgos(c: Offset, r: Float) {
    val ojo = Color(0xFF1F2937)
    drawCircle(ojo, r * 0.065f, Offset(c.x - r * 0.19f, c.y + r * 0.02f))
    drawCircle(ojo, r * 0.065f, Offset(c.x + r * 0.19f, c.y + r * 0.02f))
    drawCircle(ojo, r * 0.07f, Offset(c.x, c.y + r * 0.2f))
    drawArc(
        color = ojo, startAngle = 20f, sweepAngle = 140f, useCenter = false,
        topLeft = Offset(c.x - r * 0.13f, c.y + r * 0.17f), size = Size(r * 0.26f, r * 0.2f),
        style = Stroke(width = r * 0.04f)
    )
}
