package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
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

// Carita de perro, gato u otro animal dibujada con Canvas
@Composable
fun MascotaAvatar(especie: Especie, modifier: Modifier = Modifier, tamano: Dp = 56.dp) {
    val fondo = when (especie) {
        Especie.PERRO -> Color(0xFFFDE68A)
        Especie.GATO -> Color(0xFFFBCFE8)
        Especie.OTRO -> Color(0xFFBFDBFE)
    }
    val cara = when (especie) {
        Especie.PERRO -> Color(0xFFB45309)
        Especie.GATO -> Color(0xFF9CA3AF)
        Especie.OTRO -> Color(0xFF65A30D)
    }
    Canvas(modifier = modifier.size(tamano)) {
        val r = size.minDimension / 2
        val c = Offset(size.width / 2, size.height / 2)
        drawCircle(fondo, r, c)
        when (especie) {
            Especie.PERRO -> {
                // orejas caídas
                drawOval(cara, Offset(c.x - r * 0.75f, c.y - r * 0.45f), Size(r * 0.45f, r * 0.8f))
                drawOval(cara, Offset(c.x + r * 0.30f, c.y - r * 0.45f), Size(r * 0.45f, r * 0.8f))
            }
            Especie.GATO -> {
                // orejas en punta
                drawPath(triangulo(c.x - r * 0.55f, c.y - r * 0.15f, c.x - r * 0.45f, c.y - r * 0.8f, c.x - r * 0.1f, c.y - r * 0.4f), cara)
                drawPath(triangulo(c.x + r * 0.55f, c.y - r * 0.15f, c.x + r * 0.45f, c.y - r * 0.8f, c.x + r * 0.1f, c.y - r * 0.4f), cara)
            }
            Especie.OTRO -> {
                // orejas largas (conejo)
                drawOval(cara, Offset(c.x - r * 0.4f, c.y - r * 0.95f), Size(r * 0.28f, r * 0.75f))
                drawOval(cara, Offset(c.x + r * 0.12f, c.y - r * 0.95f), Size(r * 0.28f, r * 0.75f))
            }
        }
        drawCircle(cara, r * 0.55f, Offset(c.x, c.y + r * 0.1f))
        dibujarRasgos(c, r)
    }
}

private fun triangulo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) = Path().apply {
    moveTo(x1, y1); lineTo(x2, y2); lineTo(x3, y3); close()
}

private fun DrawScope.dibujarRasgos(c: Offset, r: Float) {
    val ojo = Color(0xFF1F2937)
    drawCircle(ojo, r * 0.07f, Offset(c.x - r * 0.2f, c.y))
    drawCircle(ojo, r * 0.07f, Offset(c.x + r * 0.2f, c.y))
    drawCircle(ojo, r * 0.08f, Offset(c.x, c.y + r * 0.2f))
    drawArc(
        color = ojo, startAngle = 20f, sweepAngle = 140f, useCenter = false,
        topLeft = Offset(c.x - r * 0.15f, c.y + r * 0.15f), size = Size(r * 0.3f, r * 0.25f),
        style = Stroke(width = r * 0.04f)
    )
}
