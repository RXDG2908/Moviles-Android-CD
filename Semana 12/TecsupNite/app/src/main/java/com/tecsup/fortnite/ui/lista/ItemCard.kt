package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.components.PrecioVBucks
import com.tecsup.fortnite.ui.theme.FormaTarjeta
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.Rayos
import com.tecsup.fortnite.ui.theme.colorRareza

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier,
    destacado: Boolean = false
) {
    val color = colorRareza(item.rarezaClave)
    Column(
        modifier
            .fillMaxWidth()
            .clip(FormaTarjeta())
            .background(Noche)
            .clickable(onClickLabel = "Ver detalle") { onClick() }
    ) {
        // Imagen sobre el color de la rareza, con rayos como en la tienda del juego
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(if (destacado) 1.55f else 1f)
                .background(Brush.verticalGradient(listOf(color, lerp(color, Noche, 0.35f))))
        ) {
            Rayos(Color.White.copy(alpha = 0.12f))
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(onClick = onFavorito, modifier = Modifier.align(Alignment.TopEnd)) {
                Box(
                    Modifier.size(34.dp).clip(CircleShape).background(Noche),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (item.esFavorito) Oro else Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        Column(Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 14.dp)) {
            Text(
                item.titulo.uppercase(),
                style = if (destacado) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                item.subtitulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
            PrecioVBucks(item.precio, item.precioRegular, Modifier.padding(top = 4.dp))
        }
    }
}
