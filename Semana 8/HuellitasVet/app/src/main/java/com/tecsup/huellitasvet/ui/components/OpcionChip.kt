package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Chip de opción del mockup: elegido = relleno del color del rol; sin elegir = blanco con borde
@Composable
fun OpcionChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    inicio: (@Composable () -> Unit)? = null
) {
    val esquema = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (seleccionado) esquema.primary else esquema.surface,
        contentColor = if (seleccionado) esquema.onPrimary else esquema.onSurfaceVariant,
        border = if (seleccionado) null else BorderStroke(1.dp, esquema.outlineVariant),
        modifier = modifier
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            inicio?.invoke()
            Text(texto, style = MaterialTheme.typography.labelLarge)
        }
    }
}
