// Iconos y efectos pequeños que se repiten en varias partes de la pantalla.
package com.leon.campusgps.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Un icono por categoría; solo decide qué dibujar, no filtra ni calcula nada
fun iconoDeCategoria(categoria: String): ImageVector = when (categoria) {
    "Estudio" -> Icons.AutoMirrored.Filled.MenuBook
    "Comida" -> Icons.Filled.Restaurant
    "Eventos" -> Icons.Filled.Event
    "Servicios" -> Icons.Filled.LocalParking
    else -> Icons.Filled.Place
}

// Punto que late, para avisar que el seguimiento en vivo está activo
@Composable
fun PuntoEnVivo(color: Color, modifier: Modifier = Modifier, tamano: Dp = 10.dp) {
    val latido = rememberInfiniteTransition(label = "latido")
    val escala by latido.animateFloat(
        initialValue = 1f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart),
        label = "escala"
    )
    val opacidad by latido.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart),
        label = "opacidad"
    )
    Box(modifier = modifier.size(tamano * 2.2f), contentAlignment = Alignment.Center) {
        // Onda que crece y se desvanece detrás del punto
        Box(
            Modifier
                .size(tamano)
                .graphicsLayer {
                    scaleX = escala
                    scaleY = escala
                    alpha = opacidad
                }
                .background(color, CircleShape)
        )
        Box(Modifier.size(tamano).background(color, CircleShape))
    }
}

// Escala que baja un poco mientras se mantiene presionado y vuelve con un resorte
@Composable
fun escalaAlPresionar(interaccion: MutableInteractionSource): Float {
    val presionado by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (presionado) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "presion"
    )
    return escala
}
