package com.tecsup.fortnite.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.PrecioVBucks
import com.tecsup.fortnite.ui.theme.colorRareza

@Composable
fun DetalleScreen(
    estado: UiState<Item>,
    onReintentar: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> ErrorView(estado.mensaje, onReintentar, modifier)
        is UiState.Exito -> Contenido(estado.datos, onFavorito, modifier)
    }
}

@Composable
private fun Contenido(item: Item, onFavorito: () -> Unit, modifier: Modifier) {
    val color = colorRareza(item.rarezaClave)
    Column(modifier.verticalScroll(rememberScrollState())) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Brush.verticalGradient(listOf(color.copy(alpha = 0.8f), MaterialTheme.colorScheme.background)))
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(16.dp)
            )
        }
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(item.titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(item.subtitulo) })
                AssistChip(onClick = {}, label = { Text(item.rareza) })
            }
            if (item.precio > 0) PrecioVBucks(item.precio, item.precioRegular)
            if (item.descripcion != "") {
                Text(item.descripcion, style = MaterialTheme.typography.bodyLarge)
            }
            if (item.serie != "") {
                Text("Serie: ${item.serie}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (item.introduccion != "") {
                Text(item.introduccion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (item.esFavorito) {
                Button(onClick = onFavorito, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Filled.Star, contentDescription = null)
                    Text("  Quitar de favoritos")
                }
            } else {
                OutlinedButton(onClick = onFavorito, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.StarBorder, contentDescription = null)
                    Text("  Agregar a favoritos")
                }
            }
        }
    }
}
