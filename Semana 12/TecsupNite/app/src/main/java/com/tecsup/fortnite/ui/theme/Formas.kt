package com.tecsup.fortnite.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

// Paralelogramo para botones, chips y etiquetas
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

// Tarjeta de la tienda: el borde de abajo baja en diagonal
class FormaTarjeta(private val corte: Dp = 12.dp) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val c = with(density) { corte.toPx() }
        val camino = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - c)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(camino)
    }
}

// Fondo de cielo azul con rayos, el mismo en toda la app
@Composable
fun FondoCielo(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Cielo, CieloFuerte)))
    ) {
        Rayos(Color.White.copy(alpha = 0.09f))
        content()
    }
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
