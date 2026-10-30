package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.TipoServicio
import com.tecsup.huellitasvet.ui.theme.colorDeTipo

fun iconoTipo(tipo: TipoServicio): ImageVector = when (tipo) {
    TipoServicio.SALUD -> Icons.Filled.Favorite
    TipoServicio.ESTETICA -> Icons.Filled.Spa
    TipoServicio.PREVENCION -> Icons.Filled.Vaccines
}

// Mejora: tarjeta para elegir el tipo de servicio.
// El color del tipo va en el ícono, el borde y el fondo suave; el texto siempre en onSurface (se lee bien).
@Composable
fun TipoServicioChip(
    tipo: TipoServicio,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cantidad: Int? = null
) {
    val color = colorDeTipo(tipo)
    val esquema = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (seleccionado) color.copy(alpha = 0.14f) else esquema.surface,
        border = BorderStroke(if (seleccionado) 2.dp else 1.dp, if (seleccionado) color else esquema.outlineVariant),
        modifier = modifier
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                Modifier.size(40.dp).background(color.copy(alpha = if (seleccionado) 1f else 0.16f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(iconoTipo(tipo), contentDescription = null, tint = if (seleccionado) esquema.surface else color, modifier = Modifier.size(22.dp))
            }
            Text(tipo.etiqueta, style = MaterialTheme.typography.labelLarge, color = esquema.onSurface)
            Text(
                cantidad?.let { "$it servicios" } ?: tipo.descripcion,
                style = MaterialTheme.typography.labelSmall,
                color = esquema.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Píldora pequeña con el tipo (en listas de servicios)
@Composable
fun TipoServicioEtiqueta(tipo: TipoServicio, modifier: Modifier = Modifier) {
    val color = colorDeTipo(tipo)
    Row(
        modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Text(tipo.etiqueta, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
    }
}
