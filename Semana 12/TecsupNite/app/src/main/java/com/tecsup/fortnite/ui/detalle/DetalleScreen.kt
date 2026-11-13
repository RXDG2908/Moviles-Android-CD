// Pantalla de detalle de un cosmetico.
package com.tecsup.fortnite.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.URL_VBUCKS
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.Borde
import com.tecsup.fortnite.ui.theme.CelesteTexto
import com.tecsup.fortnite.ui.theme.Cielo
import com.tecsup.fortnite.ui.theme.EstiloPrecio
import com.tecsup.fortnite.ui.theme.FormaInclinada
import com.tecsup.fortnite.ui.theme.Lima
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.SombraPanel
import com.tecsup.fortnite.ui.theme.TintaAm
import com.tecsup.fortnite.ui.theme.colorApi
import com.tecsup.fortnite.ui.theme.colorRareza
import com.tecsup.fortnite.ui.theme.degradadoTarjeta

private val formaHoja = RoundedCornerShape(20.dp)
private val Hi = Color(0xFFB4F1FF) // --hi de la web, para los rótulos pequeños

// Hoja del producto, como el <dialog class="hoja"> de regalitos.shop:
// imagen arriba, texto con su propio scroll y los botones siempre abajo
@Composable
fun DetalleScreen(
    estado: UiState<Item>,
    onReintentar: () -> Unit,
    onFavorito: () -> Unit,
    onCerrar: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Alto de la hoja: 86 % del alto útil de la pantalla, así nunca pisa las barras del sistema
    val alto = (LocalConfiguration.current.screenHeightDp * 0.86f).dp
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
    Box(
        modifier
            .fillMaxWidth()
            .height(alto)
            .padding(horizontal = 12.dp)
            .clip(formaHoja)
            .background(Brush.verticalGradient(listOf(Cielo, Color(0xFF0846B4))))
            .border(4.dp, Borde, formaHoja)
    ) {
        when (estado) {
            is UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorView(estado.mensaje, onReintentar)
            is UiState.Exito -> Contenido(estado.datos, onFavorito, onCerrar)
        }
        // Botón cerrar arriba a la derecha
        IconButton(onClick = onCerrar, modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
            Box(
                Modifier.size(42.dp).clip(CircleShape).background(SombraPanel.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = Blanco)
            }
        }
    }
}
}

@Composable
private fun Contenido(item: Item, onFavorito: () -> Unit, onCerrar: () -> Unit) {
    val respaldo = colorRareza(item.rarezaClave)
    val c1 = colorApi(item.color1, respaldo)
    val c3 = colorApi(item.color3, respaldo)

    Column(Modifier.fillMaxSize()) {
        // Imagen sobre los colores oficiales; abajo se funde con color3
        Box(
            Modifier
                .fillMaxWidth()
                .heightIn(min = 210.dp)
                .fillMaxHeight(0.40f)
                .background(degradadoTarjeta(c1, c3))
        ) {
            AsyncImage(
                model = item.imagenUrl,
                contentDescription = item.titulo,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(top = 8.dp)
            )
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(0.24f)
                    .background(Brush.verticalGradient(listOf(Color.Transparent, c3.copy(alpha = 0.92f))))
            )
        }

        // Texto con su propio scroll
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 12.dp)
        ) {
            // Tipo y placas, como .hoja-tipo
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    item.subtitulo.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                    color = CelesteTexto
                )
                if (item.terminaHoy) Placa("Termina hoy", Color.Black, Lima)
            }
            Text(
                item.titulo.uppercase(),
                modifier = Modifier.padding(top = 4.dp, end = 20.dp),
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 40.sp, lineHeight = 42.sp),
                color = Blanco
            )

            // Precio grande, como .hoja-precio
            if (item.precio > 0) {
                Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(model = URL_VBUCKS, contentDescription = "V-Bucks", modifier = Modifier.size(34.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "${item.precio}",
                        style = EstiloPrecio.copy(fontSize = 44.sp, lineHeight = 46.sp),
                        color = Blanco
                    )
                    if (item.precioRegular > item.precio) {
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "${item.precioRegular}",
                            style = EstiloPrecio.copy(fontSize = 22.sp),
                            color = Blanco.copy(alpha = 0.75f),
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("pavos", style = MaterialTheme.typography.titleSmall, color = Color(0xFFD7ECFF))
                }
            }
            if (item.lote != "") {
                Text(
                    "Precio del lote completo",
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

            // Datos en fichas, como los ítems de .hoja-incluye
            val datos = buildList {
                if (item.rareza != "") add("Rareza" to item.rareza)
                if (item.seccion != "") add("Sección" to item.seccion)
                if (item.serie != "" && item.serie != item.rareza) add("Serie" to item.serie)
                if (item.lote != "") add("Lote" to item.lote)
                if (item.introduccion != "") add("Llegó" to item.introduccion.removePrefix("Introducido en ").removeSuffix("."))
            }
            if (datos.isNotEmpty()) {
                Text(
                    "DETALLES",
                    modifier = Modifier.padding(top = 18.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
                    color = Hi
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for ((nombre, valor) in datos) Ficha(nombre, valor)
                }
            }

            // Ayuda, como .hoja .ayuda
            Row(Modifier.padding(top = 16.dp)) {
                Icon(Icons.Outlined.CardGiftcard, contentDescription = null, tint = Oro, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Precio de la tienda de hoy. Guárdalo en favoritos para tenerlo a la mano aunque cambie la tienda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Blanco
                )
            }
        }

        // Botones anclados abajo en 2 columnas, como .hoja-btns
        Row(
            Modifier
                .fillMaxWidth()
                .background(SombraPanel.copy(alpha = 0.35f))
                // Solo una línea arriba, como border-top de la web
                .drawBehind { drawLine(Borde.copy(alpha = 0.3f), Offset(0f, 0f), Offset(size.width, 0f), 2f) }
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onFavorito,
                shape = FormaInclinada(8.dp),
                modifier = Modifier.weight(1.5f).height(50.dp),
                colors = if (item.esFavorito) ButtonDefaults.buttonColors(containerColor = Blanco, contentColor = TintaAm)
                else ButtonDefaults.buttonColors(containerColor = Oro, contentColor = TintaAm)
            ) {
                Icon(if (item.esFavorito) Icons.Filled.Star else Icons.Outlined.StarBorder, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(
                    if (item.esFavorito) " EN FAVORITOS" else " A FAVORITOS",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            OutlinedButton(
                onClick = onCerrar,
                shape = FormaInclinada(8.dp),
                modifier = Modifier.weight(1f).height(50.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Blanco),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blanco)
            ) {
                Text("VOLVER", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

// Ficha de un dato: rótulo pequeño y valor
@Composable
private fun Ficha(nombre: String, valor: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x7305168A))
            .border(1.dp, Borde.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(nombre, modifier = Modifier.width(72.dp), style = MaterialTheme.typography.labelMedium, color = CelesteTexto)
        Text(valor, style = MaterialTheme.typography.titleSmall, color = Blanco)
    }
}

// Placa como .chapa de la web
@Composable
private fun Placa(texto: String, fondo: Color, letra: Color) {
    Text(
        texto.uppercase(),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(fondo)
            .padding(horizontal = 7.dp, vertical = 4.dp),
        style = EstiloPrecio.copy(fontSize = 10.sp, lineHeight = 11.sp),
        color = letra
    )
}

