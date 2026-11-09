package com.tecsup.fortnite.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

const val URL_VBUCKS = "https://fortnite-api.com/images/vbuck.png"

// Precio con icono de V-Bucks; si hay descuento muestra el precio normal tachado
@Composable
fun PrecioVBucks(precio: Int, precioRegular: Int, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = URL_VBUCKS, contentDescription = "V-Bucks", modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(5.dp))
        Text(
            "$precio",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (precioRegular > precio) {
            Spacer(Modifier.width(8.dp))
            Text(
                "$precioRegular",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}
