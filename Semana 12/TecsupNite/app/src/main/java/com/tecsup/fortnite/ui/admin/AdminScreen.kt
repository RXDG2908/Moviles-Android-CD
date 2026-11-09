package com.tecsup.fortnite.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView

@Composable
fun AdminScreen(
    estado: UiState<Totales>,
    mensaje: String?,
    onSincronizar: () -> Unit,
    onLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> ErrorView(estado.mensaje, onSincronizar, modifier)
        is UiState.Exito -> Column(
            modifier.verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Panel de administración", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Total("Guardados", estado.datos.guardados, Modifier.weight(1f))
                Total("Favoritos", estado.datos.favoritos, Modifier.weight(1f))
            }
            Button(onClick = onSincronizar, modifier = Modifier.fillMaxWidth()) { Text("Sincronizar") }
            OutlinedButton(onClick = onLimpiar, modifier = Modifier.fillMaxWidth()) { Text("Limpiar caché") }
            if (mensaje != null) {
                Text(mensaje, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun Total(titulo: String, valor: Int, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("$valor", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(titulo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
