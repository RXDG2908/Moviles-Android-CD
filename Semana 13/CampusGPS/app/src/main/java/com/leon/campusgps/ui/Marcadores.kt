// Dibujos de los marcadores del mapa: tu posición ("Tú") y los pines de cada lugar.
// Se pintan con Compose y el mapa los convierte en imagen (MarkerComposable).
package com.leon.campusgps.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ColorTu = Color(0xFFE53935)
val ColorPin = Color(0xFF1E88E5)
val ColorPinCercano = Color(0xFF2E7D32)

// Medidas del marcador "Tú" (para calcular su punto de anclaje en el mapa)
private const val ALTO_ETIQUETA = 22
private const val SEPARACION = 3
private const val DIAMETRO_TU = 42

// El mapa clava el marcador en el centro del círculo, no en la etiqueta de arriba
val AnclaTu = Offset(
    0.5f,
    (ALTO_ETIQUETA + SEPARACION + DIAMETRO_TU / 2f) / (ALTO_ETIQUETA + SEPARACION + DIAMETRO_TU)
)

// Tu posición: una etiqueta "Tú" encima de un círculo rojo con una persona
@Composable
fun MarcadorTu() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(ALTO_ETIQUETA.dp)
                .background(Color(0xFF212121), RoundedCornerShape(50))
                .padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Tú", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(SEPARACION.dp))
        Box(
            modifier = Modifier
                .size(DIAMETRO_TU.dp)
                .background(ColorTu, CircleShape)
                .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

// Pin de un lugar: la gota de color de siempre, pero con el icono de su categoría
// (cubiertos, libro, calendario...) donde antes iba el punto del centro
@Composable
fun PinLugar(categoria: String, color: Color, elegido: Boolean) {
    // El pin del lugar elegido se ve un poco más grande
    val ancho = if (elegido) 46.dp else 38.dp
    val alto = ancho * 1.32f
    Box(modifier = Modifier.size(ancho, alto)) {
        Canvas(Modifier.size(ancho, alto)) {
            val r = size.width / 2f
            val cx = size.width / 2f
            // Gota = círculo de arriba + triángulo que termina en la punta de abajo
            val circulo = Path().apply { addOval(Rect(Offset(cx, r), r * 0.94f)) }
            val punta = Path().apply {
                moveTo(cx - r * 0.66f, r + r * 0.62f)
                lineTo(cx + r * 0.66f, r + r * 0.62f)
                lineTo(cx, size.height - 1f)
                close()
            }
            val gota = Path.combine(PathOperation.Union, circulo, punta)
            drawPath(gota, color)
            drawPath(gota, Color.White, style = Stroke(width = 2.dp.toPx()))
        }
        // El icono va centrado en la parte redonda de la gota
        Box(
            modifier = Modifier.size(ancho),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                iconoDeCategoria(categoria),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(ancho * 0.5f)
            )
        }
    }
}
