package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.theme.EstadoAtendida
import com.tecsup.huellitasvet.ui.theme.EstadoCancelada
import com.tecsup.huellitasvet.ui.theme.EstadoConfirmada
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente

fun colorDeEstado(estado: EstadoCita): Color = when (estado) {
    EstadoCita.PENDIENTE -> EstadoPendiente
    EstadoCita.CONFIRMADA -> EstadoConfirmada
    EstadoCita.ATENDIDA -> EstadoAtendida
    EstadoCita.CANCELADA -> EstadoCancelada
}

// Píldora con el estado. 'solido' = fondo blanco (para usar sobre la tarjeta del color del rol)
@Composable
fun EstadoCitaChip(estado: EstadoCita, modifier: Modifier = Modifier, solido: Boolean = false) {
    val color = colorDeEstado(estado)
    val forma = RoundedCornerShape(50)
    Text(
        text = estado.etiqueta,
        color = color,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, forma)
            .background(if (solido) Color.Transparent else color.copy(alpha = 0.14f), forma)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
