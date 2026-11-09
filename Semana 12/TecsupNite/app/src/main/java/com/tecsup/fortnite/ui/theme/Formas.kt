package com.tecsup.fortnite.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

// Paralelogramo para los chips y las etiquetas
class FormaInclinada(private val corte: Dp = 10.dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val c = with(density) { corte.toPx() }
        val camino = Path().apply {
            moveTo(c, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width - c, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(camino)
    }
}

// Fondo de cielo azul, más claro arriba, con rayos suaves
@Composable
fun FondoCielo(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CieloClaro, Cielo, CieloFuerte)))
    ) {
        Rayos(Color.White.copy(alpha = 0.06f))
        content()
    }
}

// Fondo de una tarjeta: color de la rareza, con luz en el centro y borde más oscuro
fun Modifier.fondoRareza(color: Color): Modifier = drawBehind {
    drawRect(lerp(color, Color.Black, 0.25f))
    drawRect(
        Brush.radialGradient(
            colors = listOf(lerp(color, Color.White, 0.30f), color, color.copy(alpha = 0f)),
            center = Offset(size.width / 2f, size.height * 0.42f),
            radius = max(size.width, size.height) * 0.75f
        )
    )
}

// Rayos que salen de arriba, dibujados como triángulos
@Composable
fun Rayos(color: Color, modifier: Modifier = Modifier) {
    // clipToBounds evita que los rayos se salgan de su recuadro
    Canvas(modifier.fillMaxSize().clipToBounds()) {
        val origen = Offset(size.width * 0.5f, -size.height * 0.08f)
        val largo = size.height * 1.6f
        val total = 14
        val paso = (Math.PI / total).toFloat()
        for (i in 0 until total step 2) {
            val a1 = i * paso
            val a2 = a1 + paso
            val rayo = Path().apply {
                moveTo(origen.x, origen.y)
                lineTo(origen.x + cos(a1) * largo, origen.y + sin(a1) * largo)
                lineTo(origen.x + cos(a2) * largo, origen.y + sin(a2) * largo)
                close()
            }
            drawPath(rayo, color)
        }
    }
}
