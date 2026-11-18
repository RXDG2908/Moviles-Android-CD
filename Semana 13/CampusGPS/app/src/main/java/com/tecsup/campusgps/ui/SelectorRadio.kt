// Fila de chips para elegir el radio de cercanía (100, 200 o 500 m).
package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Radar,
            contentDescription = "Radio",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text("Radio", style = MaterialTheme.typography.labelLarge)
        radios.forEach { metros ->
            FilterChip(
                selected = elegido == metros,
                onClick = {
                    vibracion.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    // Tocar el radio ya elegido lo quita
                    onElegir(if (elegido == metros) null else metros)
                },
                label = { Text("$metros m") }
            )
        }
    }
}
