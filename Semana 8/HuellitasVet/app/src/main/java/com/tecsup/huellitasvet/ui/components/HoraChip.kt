package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

// Selector de hora. 'ocupado' = gris y tachado, no se puede tocar
@Composable
fun HoraChip(
    hora: String,
    seleccionada: Boolean,
    ocupado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val esquema = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        enabled = !ocupado,
        shape = RoundedCornerShape(12.dp),
        color = when {
            ocupado -> esquema.surfaceVariant
            seleccionada -> esquema.primary
            else -> esquema.surface
        },
        contentColor = when {
            ocupado -> esquema.onSurfaceVariant.copy(alpha = 0.5f)
            seleccionada -> esquema.onPrimary
            else -> esquema.onSurface
        },
        border = if (seleccionada || ocupado) null else BorderStroke(1.dp, esquema.outlineVariant),
        modifier = modifier
    ) {
        Text(
            hora,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            textDecoration = if (ocupado) TextDecoration.LineThrough else null,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}
