// Formas (esquinas) usadas en la interfaz.
package com.tecsup.fortnite.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

// Paralelogramo sin radio, como los botones y placas de la web (skew -6°)
class FormaInclinada(private val corte: Dp = 6.dp) : Shape {
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

// Fondo de la app: degradado azul de la tienda, sin dibujos
@Composable
fun FondoCielo(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(CieloClaro, Cielo, CieloFuerte)))
    ) {
        content()
    }
}

// Convierte "RRGGBBAA" de la API a Color; si viene vacío usa el de respaldo
fun colorApi(hex: String, respaldo: Color): Color = try {
    val rgb = hex.take(6).toLong(16)
    Color(0xFF000000 or rgb)
} catch (e: Exception) {
    respaldo
}

// Degradado de la tarjeta como .card-fondo: color1 -> mezcla -> color3, con luz arriba
fun degradadoTarjeta(c1: Color, c3: Color): Brush = Brush.linearGradient(
    colors = listOf(lerp(c1, Color.White, 0.18f), c1, lerp(c1, c3, 0.55f), c3)
)
