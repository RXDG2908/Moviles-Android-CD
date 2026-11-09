package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.components.PrecioVBucks
import com.tecsup.fortnite.ui.theme.colorRareza

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = colorRareza(item.rarezaClave)
    Card(
        modifier = modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Imagen sobre un degradado del color de la rareza
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(
                    Brush.verticalGradient(
                        listOf(color.copy(alpha = 0.75f), color.copy(alpha = 0.15f))
                    )
                )
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(6.dp)
            )
            IconButton(onClick = onFavorito, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(
                    if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (item.esFavorito) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Column(Modifier.padding(12.dp)) {
            Text(
                item.titulo,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                item.subtitulo,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                maxLines = 1
            )
            PrecioVBucks(item.precio, item.precioRegular, Modifier.padding(top = 6.dp))
        }
    }
}
