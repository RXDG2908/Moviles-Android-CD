// Pantalla de detalle: la publicación completa y la lista de sus comentarios.
package com.leon.murocampus.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.viewmodel.PostsViewModel
import com.leon.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(vm: PostsViewModel, id: Int) {
    val estado by vm.detalle.collectAsStateWithLifecycle()

    when (val e = estado) {
        UiState.Cargando -> Cargando("Cargando publicación…")
        is UiState.Error -> ErrorConReintento(e.mensaje) { vm.abrir(id) }
        is UiState.Exito -> LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "Usuario ${e.datos.post.userId}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    e.datos.post.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(e.datos.post.body, style = MaterialTheme.typography.bodyLarge)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(
                    "Comentarios (${e.datos.comentarios.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            items(e.datos.comentarios, key = { it.id }) { c ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // El correo va en azul, como pide el diseño
                        Text(
                            c.email,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(c.body, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
