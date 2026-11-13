// Pantalla del admin: totales, sincronizar y limpiar cache.
package com.tecsup.fortnite.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.FormaInclinada
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.Rojo

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
            modifier
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Total("Guardados", estado.datos.guardados, Modifier.weight(1f))
                Total("Favoritos", estado.datos.favoritos, Modifier.weight(1f))
            }
            if (mensaje != null) {
                Text(
                    mensaje,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Blanco)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = Noche
                )
            }
            Button(
                onClick = onSincronizar,
                shape = FormaInclinada(10.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(58.dp)
            ) {
                Icon(Icons.Filled.Sync, contentDescription = null)
                Text("  SINCRONIZAR TIENDA", style = MaterialTheme.typography.titleMedium)
            }
            Button(
                onClick = onLimpiar,
                shape = FormaInclinada(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Rojo, contentColor = Blanco),
                modifier = Modifier.fillMaxWidth().height(58.dp)
            ) {
                Icon(Icons.Filled.DeleteSweep, contentDescription = null)
                Text("  LIMPIAR CACHÉ", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun Total(titulo: String, valor: Int, modifier: Modifier) {
    Column(
        modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Noche)
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Text("$valor", style = MaterialTheme.typography.displayMedium, color = Oro)
        Text(titulo.uppercase(), style = MaterialTheme.typography.titleSmall, color = Blanco)
    }
}
