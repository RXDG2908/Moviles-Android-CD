package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.components.PrecioVBucks
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.colorRareza
import com.tecsup.fortnite.ui.theme.fondoRareza

private val forma = RoundedCornerShape(12.dp)

// Tarjeta como las de la tienda del juego: la imagen ocupa todo y el nombre va encima
@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier,
    destacado: Boolean = false
) {
    val color = colorRareza(item.rarezaClave)
    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(if (destacado) 1.15f else 0.78f)
            .clip(forma)
            .fondoRareza(color)
            .border(2.dp, color, forma)
            .clickable(onClickLabel = "Ver detalle") { onClick() }
    ) {
        AsyncImage(
            model = item.imagenUrl,
            contentDescription = item.titulo,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        )

        // Etiquetas arriba a la izquierda
        Row(Modifier.align(Alignment.TopStart).padding(8.dp)) {
            if (item.lote != "") Etiqueta("LOTE", Oro, Noche)
            if (item.precioRegular > item.precio && item.precioRegular > 0) {
                val descuento = 100 - item.precio * 100 / item.precioRegular
                Etiqueta("-$descuento%", Blanco, Noche)
            }
        }

        IconButton(onClick = onFavorito, modifier = Modifier.align(Alignment.TopEnd)) {
            Box(
                Modifier.size(34.dp).clip(CircleShape).background(Noche.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (item.esFavorito) Oro else Blanco,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Nombre y precio sobre una sombra para que se lean encima de cualquier imagen
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Noche.copy(alpha = 0.92f))))
                .padding(start = 10.dp, end = 10.dp, top = 28.dp, bottom = 10.dp)
        ) {
            Text(
                item.titulo.uppercase(),
                style = if (destacado) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleMedium,
                color = Blanco,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                item.subtitulo,
                style = MaterialTheme.typography.labelMedium,
                color = Blanco.copy(alpha = 0.8f),
                maxLines = 1
            )
            PrecioVBucks(item.precio, item.precioRegular, Modifier.padding(top = 2.dp))
        }

        // Franja del color de la rareza abajo
        Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(5.dp).background(color))
    }
}

@Composable
private fun Etiqueta(texto: String, fondo: Color, letra: Color) {
    Text(
        texto,
        modifier = Modifier
            .padding(end = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(fondo)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        style = MaterialTheme.typography.labelLarge,
        color = letra
    )
}
