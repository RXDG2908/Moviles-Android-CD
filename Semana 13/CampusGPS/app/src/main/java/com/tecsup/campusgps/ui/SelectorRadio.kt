// Fila de chips para elegir el radio de cercanía (100, 200 o 500 m).
package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SelectorRadio(
    radios: List<Int>,
    elegido: Int?,
    onElegir: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Radio:")
        radios.forEach { metros ->
            FilterChip(
                selected = elegido == metros,
                // Tocar el radio ya elegido lo quita
                onClick = { onElegir(if (elegido == metros) null else metros) },
                label = { Text("$metros m") }
            )
        }
    }
}
