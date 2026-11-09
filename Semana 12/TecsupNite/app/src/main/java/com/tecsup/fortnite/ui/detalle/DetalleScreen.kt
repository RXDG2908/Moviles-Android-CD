package com.tecsup.fortnite.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.PrecioVBucks
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.FormaInclinada
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.NocheClara
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.Rayos
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
    Column(
        modifier
            .fillMaxSize()
            .background(Noche)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(max = 420.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = false)
                .background(Brush.verticalGradient(listOf(color, lerp(color, Noche, 0.45f))))
        ) {
            Rayos(Color.White.copy(alpha = 0.12f))
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(Modifier.padding(horizontal = 18.dp, vertical = 16.dp)) {
            Text(
                item.titulo.uppercase(),
                style = MaterialTheme.typography.displaySmall,
                color = Blanco
            )
            Row(
                Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Etiqueta(item.subtitulo, NocheClara)
                if (item.rareza != "") Etiqueta(item.rareza, color)
            }
            if (item.precio > 0) {
                PrecioVBucks(item.precio, item.precioRegular, Modifier.padding(top = 14.dp))
            }
            if (item.lote != "") {
                Text(
                    "El precio es del lote completo: ${item.lote}",
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Oro
                )
            }
            if (item.descripcion != "") {
                Text(
                    item.descripcion,
                    modifier = Modifier.padding(top = 14.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Blanco
                )
            }
            if (item.serie != "") {
                Text(
                    "Serie: ${item.serie}",
                    modifier = Modifier.padding(top = 10.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (item.introduccion != "") {
                Text(
                    item.introduccion,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onFavorito,
                shape = FormaInclinada(14.dp),
                modifier = Modifier.fillMaxWidth().height(58.dp),
                colors = if (item.esFavorito) {
                    ButtonDefaults.buttonColors(containerColor = NocheClara, contentColor = Blanco)
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Icon(if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder, contentDescription = null)
                Text(
                    if (item.esFavorito) "  QUITAR DE FAVORITOS" else "  AGREGAR A FAVORITOS",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// No se toca: solo informa el tipo o la rareza
@Composable
private fun Etiqueta(texto: String, fondo: Color) {
    Text(
        texto.uppercase(),
        modifier = Modifier
            .clip(FormaInclinada(8.dp))
            .background(fondo)
            .padding(horizontal = 14.dp, vertical = 5.dp),
        style = MaterialTheme.typography.titleSmall,
        color = Blanco
    )
}
