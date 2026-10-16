package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Botón principal del color del rol (MaterialTheme.colorScheme.primary)
@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    secundario: Boolean = false
) {
    val m = modifier.fillMaxWidth().height(52.dp)
    if (secundario) {
        OutlinedButton(onClick = onClick, modifier = m, enabled = habilitado, shape = RoundedCornerShape(14.dp)) {
            Text(texto)
        }
    } else {
        Button(onClick = onClick, modifier = m, enabled = habilitado, shape = RoundedCornerShape(14.dp)) {
            Text(texto)
        }
    }
}
