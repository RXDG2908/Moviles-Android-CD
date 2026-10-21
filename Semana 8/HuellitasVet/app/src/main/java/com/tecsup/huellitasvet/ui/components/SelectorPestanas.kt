package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

// Selector tipo píldora (Dueño / Veterinario, Próximas / Historial, Datos / Historial clínico).
// relleno = true: la opción elegida va del color del rol (login).
// relleno = false: la opción elegida va en blanco con texto del color del rol.
@Composable
fun SelectorPestanas(
    opciones: List<String>,
    seleccionada: Int,
    onSeleccion: (Int) -> Unit,
    modifier: Modifier = Modifier,
    relleno: Boolean = false
) {
    val esquema = MaterialTheme.colorScheme
    Row(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(esquema.surfaceVariant)
            .padding(4.dp)
    ) {
        opciones.forEachIndexed { i, texto ->
            val activa = i == seleccionada
            val fondo = when {
                activa && relleno -> esquema.primary
                activa -> esquema.surface
                else -> esquema.surfaceVariant
            }
            val color = when {
                activa && relleno -> esquema.onPrimary
                activa -> esquema.primary
                else -> esquema.onSurfaceVariant
            }
            Box(
                Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(fondo)
                    .clickable { onSeleccion(i) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(texto, color = color, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
