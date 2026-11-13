// Tarjeta de un item en la lista.
package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.components.URL_VBUCKS
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.EstiloPrecio
import com.tecsup.fortnite.ui.theme.Lima
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.SombraPanel
import com.tecsup.fortnite.ui.theme.TintaAm
import com.tecsup.fortnite.ui.theme.colorApi
import com.tecsup.fortnite.ui.theme.colorRareza
import com.tecsup.fortnite.ui.theme.degradadoTarjeta

private val forma = RoundedCornerShape(16.dp)

// Tarjeta como la Plantilla Prime de la web: el fondo de color ocupa el 85 % de abajo,
// la imagen asoma por arriba y solo lleva la etiqueta y el precio (el nombre va en el detalle)
@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier,
    ancho: Boolean = false
) {
    val respaldo = colorRareza(item.rarezaClave)
    val c1 = colorApi(item.color1, respaldo)
    val c3 = colorApi(item.color3, respaldo)

    Box(
        modifier
            .fillMaxWidth()
            .aspectRatio(if (ancho) 1.2f else 0.627f)
            .semantics { contentDescription = "${item.titulo}, ${item.subtitulo}, ${item.precio} pavos" }
            .clickable(onClickLabel = "Ver detalle") { onClick() }
    ) {
        // Fondo de color (85 % de abajo)
        Box(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .clip(forma)
                .background(degradadoTarjeta(c1, c3))
                .border(1.5.dp, Color.White.copy(alpha = 0.16f), forma)
        )

        // Imagen: puede salir por arriba y se difumina abajo para leer el precio
        AsyncImage(
            model = item.imagenUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            0f to Color.Black, 0.64f to Color.Black, 0.9f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn
                    )
                }
        )

        // Pie: placas, etiqueta y precio
        Column(
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 12.dp, end = 62.dp, bottom = 14.dp)
        ) {
            Row {
                if (item.precioRegular > item.precio && item.precioRegular > 0) {
                    Placa("${item.precioRegular - item.precio} de descuento", Blanco, Color.Black)
                    Spacer(Modifier.width(5.dp))
                }
                if (item.terminaHoy) Placa("Termina hoy", Color.Black, Lima)
            }
            Text(
                item.subtitulo.uppercase(),
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.6.sp),
                color = Blanco.copy(alpha = 0.92f),
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(model = URL_VBUCKS, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(
                    " ${item.precio}",
                    style = EstiloPrecio,
                    color = Blanco
                )
                if (item.precioRegular > item.precio) {
                    Text(
                        " ${item.precioRegular}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Blanco.copy(alpha = 0.7f),
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }

        // Botón amarillo abajo a la derecha (en la web es "+"; aquí guarda en favoritos)
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 12.dp)
                .size(48.dp) // área táctil mínima de Android
                .clip(CircleShape)
                .background(Oro)
                .clickable(onClickLabel = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos") { onFavorito() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = if (item.esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                tint = TintaAm,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// Placa como .chapa de la web: blanca para descuento, negra con lima para "Termina hoy"
@Composable
private fun Placa(texto: String, fondo: Color, letra: Color) {
    Text(
        texto.uppercase(),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(fondo)
            .padding(horizontal = 7.dp, vertical = 4.dp),
        style = EstiloPrecio.copy(fontSize = 10.sp, lineHeight = 11.sp),
        color = letra,
        maxLines = 1
    )
}
