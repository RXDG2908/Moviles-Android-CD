package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Tarjeta del mockup: fondo blanco (surface), borde fino y esquinas redondeadas.
// Con 'seleccionada' toma el color del contenedor y un borde del color del rol.
@Composable
fun Tarjeta(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    seleccionada: Boolean = false,
    fondo: Color? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val esquema = MaterialTheme.colorScheme
    val colores = CardDefaults.cardColors(
        containerColor = fondo ?: if (seleccionada) esquema.primaryContainer else esquema.surface
    )
    val borde = when {
        seleccionada -> BorderStroke(2.dp, esquema.primary)
        fondo != null -> null
        else -> BorderStroke(1.dp, esquema.outlineVariant)
    }
    val forma = RoundedCornerShape(16.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = forma, colors = colores, border = borde, content = contenido)
    } else {
        Card(modifier = modifier.fillMaxWidth(), shape = forma, colors = colores, border = borde, content = contenido)
    }
}
