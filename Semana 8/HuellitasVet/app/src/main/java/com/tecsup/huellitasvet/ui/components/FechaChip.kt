package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Fechas
import java.time.LocalDate

// Selector de fecha (día de la semana y número)
@Composable
fun FechaChip(fecha: LocalDate, seleccionada: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val esquema = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = if (seleccionada) esquema.primary else esquema.surface,
        contentColor = if (seleccionada) esquema.onPrimary else esquema.onSurface,
        border = if (seleccionada) null else BorderStroke(1.dp, esquema.outlineVariant),
        modifier = modifier.width(58.dp)
    ) {
        Column(Modifier.padding(vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                Fechas.diaSemana(fecha),
                style = MaterialTheme.typography.labelMedium,
                color = if (seleccionada) esquema.onPrimary else esquema.onSurfaceVariant
            )
            Text(fecha.dayOfMonth.toString(), style = MaterialTheme.typography.titleMedium)
        }
    }
}
