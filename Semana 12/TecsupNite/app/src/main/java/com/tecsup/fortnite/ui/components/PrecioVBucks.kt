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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

const val URL_VBUCKS = "https://fortnite-api.com/images/vbuck.png"

// Precio con icono de V-Bucks; si hay descuento muestra el precio normal tachado
@Composable
fun PrecioVBucks(precio: Int, precioRegular: Int, modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(model = URL_VBUCKS, contentDescription = "V-Bucks", modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            "$precio",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
        if (precioRegular > precio) {
            Spacer(Modifier.width(6.dp))
            Text(
                "$precioRegular",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}
