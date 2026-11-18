// Fila de chips para filtrar los lugares por categoría.
package com.tecsup.campusgps.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FiltroCategorias(
    categorias: List<String>,
    elegida: String?,
    onElegir: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Con scroll horizontal por si los chips no entran en pantallas angostas
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = elegida == null,
            onClick = { onElegir(null) },
            label = { Text("Todas") }
        )
        categorias.forEach { cat ->
            FilterChip(
                selected = elegida == cat,
                // Tocar el chip ya elegido lo quita y vuelve a mostrar todas
                onClick = { onElegir(if (elegida == cat) null else cat) },
                label = { Text(cat) }
            )
        }
    }
}
