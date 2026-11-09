package com.tecsup.fortnite.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Fila("Cosméticos guardados", estado.datos.guardados)
            Fila("Favoritos", estado.datos.favoritos)
            Button(
                onClick = onSincronizar,
                shape = FormaInclinada(14.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp).height(60.dp)
            ) {
                Text("SINCRONIZAR", style = MaterialTheme.typography.headlineSmall)
            }
            Button(
                onClick = onLimpiar,
                shape = FormaInclinada(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Rojo, contentColor = Blanco),
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("LIMPIAR CACHÉ", style = MaterialTheme.typography.headlineSmall)
            }
            if (mensaje != null) {
                Text(
                    mensaje.uppercase(),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Blanco
                )
            }
        }
    }
}

@Composable
private fun Fila(titulo: String, valor: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(FormaInclinada(14.dp))
            .background(Noche)
            .padding(horizontal = 26.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(titulo.uppercase(), style = MaterialTheme.typography.titleMedium, color = Blanco)
        Text("$valor", style = MaterialTheme.typography.displaySmall, color = Oro)
    }
}
