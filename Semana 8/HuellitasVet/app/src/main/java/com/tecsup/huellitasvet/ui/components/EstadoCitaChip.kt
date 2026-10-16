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

@Composable
fun EstadoCitaChip(estado: EstadoCita, modifier: Modifier = Modifier) {
    val color = colorDeEstado(estado)
    Text(
        text = estado.etiqueta,
        color = color,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(Color.White, RoundedCornerShape(50)) // fondo blanco: se lee sobre cualquier tarjeta
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
