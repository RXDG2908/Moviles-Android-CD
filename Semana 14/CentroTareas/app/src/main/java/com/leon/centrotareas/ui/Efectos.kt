// Efectos y piezas visuales que se repiten en la pantalla: punto que late, icono que gira y sacudida.
package com.leon.centrotareas.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Verde de los resultados correctos (fondo y texto)
val VerdeExito = Color(0xFF2E7D32)
val FondoExito = Color(0xFFE3F4E4)

// Punto que late, para avisar que algo está vivo o trabajando
@Composable
fun PuntoLatiendo(color: Color, modifier: Modifier = Modifier, tamano: Dp = 8.dp) {
    val latido = rememberInfiniteTransition(label = "latido")
    val escala by latido.animateFloat(
        initialValue = 1f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "escala"
    )
    val opacidad by latido.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "opacidad"
    )
    Box(modifier = modifier.size(tamano * 2.4f), contentAlignment = Alignment.Center) {
        // Onda que crece y se desvanece
        Box(
            modifier = Modifier
                .size(tamano)
                .graphicsLayer {
                    scaleX = escala
                    scaleY = escala
                    alpha = opacidad
                }
                .background(color, CircleShape)
        )
        Box(modifier = Modifier.size(tamano).background(color, CircleShape))
    }
}

// Icono que gira sin parar mientras se muestra (reloj de arena, sincronizar)
@Composable
fun IconoGirando(icono: ImageVector, color: Color, modifier: Modifier = Modifier) {
    val giro = rememberInfiniteTransition(label = "giro")
    val angulo by giro.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing)),
        label = "angulo"
    )
    Icon(icono, contentDescription = null, tint = color, modifier = modifier.rotate(angulo))
}

// Sacude su contenido de lado a lado una vez al aparecer (para los errores)
@Composable
fun Sacudida(clave: Any, contenido: @Composable () -> Unit) {
    val desplazamiento = remember(clave) { Animatable(0f) }
    LaunchedEffect(clave) {
        for (x in listOf(18f, -14f, 10f, -6f, 3f, 0f)) {
            desplazamiento.animateTo(x, tween(55))
        }
    }
    Box(modifier = Modifier.graphicsLayer { translationX = desplazamiento.value }) {
        contenido()
    }
}

// Card con icono en un círculo de color y título; entra desde abajo con un pequeño retraso
@Composable
fun TarjetaTarea(
    titulo: String,
    icono: ImageVector,
    colorIcono: Color,
    orden: Int = 0,
    contenido: @Composable () -> Unit
) {
    val visible = remember { MutableTransitionState(false).apply { targetState = true } }
    AnimatedVisibility(
        visibleState = visible,
        enter = fadeIn(tween(450, delayMillis = orden * 120)) +
            slideInVertically(tween(450, delayMillis = orden * 120)) { it / 3 }
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(colorIcono.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icono, contentDescription = null, tint = colorIcono)
                    }
                    Text(
                        titulo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                contenido()
            }
        }
    }
}

// Fila con icono y texto, usada en los resultados
@Composable
fun FilaIcono(icono: ImageVector, texto: String, color: Color, negrita: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Text(
            texto,
            color = color,
            fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
