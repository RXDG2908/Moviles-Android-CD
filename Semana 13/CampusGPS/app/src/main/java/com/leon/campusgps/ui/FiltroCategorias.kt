// Fila de chips para filtrar los lugares por categoría.
package com.leon.campusgps.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun FiltroCategorias(
    categorias: List<String>,
    elegida: String?,
    onElegir: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val vibracion = LocalHapticFeedback.current

    // Con scroll horizontal por si los chips no entran en pantallas angostas
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChipConIcono("Todas", Icons.Filled.Apps, elegida == null) {
            vibracion.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onElegir(null)
        }
        categorias.forEach { cat ->
            ChipConIcono(cat, iconoDeCategoria(cat), elegida == cat) {
                vibracion.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                // Tocar el chip ya elegido lo quita y vuelve a mostrar todas
                onElegir(if (elegida == cat) null else cat)
            }
        }
    }
}

@Composable
private fun ChipConIcono(texto: String, icono: ImageVector, elegido: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = elegido,
        onClick = onClick,
        label = { Text(texto) },
        leadingIcon = {
            Icon(icono, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize))
        }
    )
}
