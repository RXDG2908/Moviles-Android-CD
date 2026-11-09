package com.tecsup.fortnite.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.PrecioVBucks
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.NocheClara
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.colorRareza
import com.tecsup.fortnite.ui.theme.fondoRareza

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
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp)
    ) {
        // Imagen grande sobre el color de la rareza
        Box(
            Modifier
                .fillMaxWidth()
                .height(380.dp)
                .clip(RoundedCornerShape(16.dp))
                .fondoRareza(color)
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(6.dp).background(color))
        }

        // Ficha con los datos, sube un poco sobre la imagen
        Column(
            Modifier
                .offset(y = (-14).dp)
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Noche)
                .padding(18.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Etiqueta(item.subtitulo, NocheClara)
                if (item.rareza != "") Etiqueta(item.rareza, color)
            }
            Text(
                item.titulo.uppercase(),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.displaySmall,
                color = Blanco
            )
            if (item.descripcion != "") {
                Text(
                    item.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (item.precio > 0) {
                PrecioVBucks(item.precio, item.precioRegular, Modifier.padding(top = 14.dp))
            }
            if (item.lote != "") {
                Text(
                    "El precio es del lote completo: ${item.lote}",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Oro
                )
            }
            if (item.serie != "" || item.introduccion != "") {
                Spacer(Modifier.height(12.dp))
                if (item.serie != "") Dato("Serie", item.serie)
                if (item.introduccion != "") Dato("Llegó", item.introduccion)
            }
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = onFavorito,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp),
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

@Composable
private fun Dato(nombre: String, valor: String) {
    Row(Modifier.padding(top = 2.dp)) {
        Text("$nombre: ", style = MaterialTheme.typography.bodyMedium, color = Oro)
        Text(valor, style = MaterialTheme.typography.bodyMedium, color = Blanco)
    }
}

// No se toca: solo informa el tipo o la rareza
@Composable
private fun Etiqueta(texto: String, fondo: Color) {
    Text(
        texto.uppercase(),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(fondo)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        style = MaterialTheme.typography.titleSmall,
        color = Blanco
    )
}
