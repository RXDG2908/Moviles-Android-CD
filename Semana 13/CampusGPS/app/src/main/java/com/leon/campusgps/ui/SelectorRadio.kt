// Fila de chips para elegir el radio de cercanía (100, 200 o 500 m).
package com.leon.campusgps.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun SelectorRadio(
    radios: List<Int>,
    elegido: Int?,
    onElegir: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val vibracion = LocalHapticFeedback.current

    // El icono de radar gira mientras hay un radio activo y se detiene al quitarlo
    val giro = rememberInfiniteTransition(label = "giroRadar")
    val angulo by giro.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart),
        label = "angulo"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Radar,
            contentDescription = "Radio",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(20.dp)
                .rotate(if (elegido != null) angulo else 0f)
        )
        Text("Radio", style = MaterialTheme.typography.labelLarge)
        radios.forEach { metros ->
            val activo = elegido == metros
            // El chip elegido da un pequeño salto con rebote
            val escala by animateFloatAsState(
                targetValue = if (activo) 1.06f else 1f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium),
                label = "escalaChip"
            )
            FilterChip(
                selected = activo,
                onClick = {
                    vibracion.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    // Tocar el radio ya elegido lo quita
                    onElegir(if (activo) null else metros)
                },
                label = { Text("$metros m") },
                // La palomita entra creciendo y sale encogiéndose
                leadingIcon = {
                    AnimatedVisibility(
                        visible = activo,
                        enter = expandHorizontally() + scaleIn() + fadeIn(),
                        exit = shrinkHorizontally() + scaleOut() + fadeOut()
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                },
                modifier = Modifier.scale(escala)
            )
        }
    }
}
