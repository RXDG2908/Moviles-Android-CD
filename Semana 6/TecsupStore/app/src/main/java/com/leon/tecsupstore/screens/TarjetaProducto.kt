package com.leon.tecsupstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupstore.data.Producto
import com.leon.tecsupstore.ui.theme.LilaClaro
import com.leon.tecsupstore.ui.theme.LilaTecsup

// Tarjeta de un producto: imagen, nombre y precio
@Composable
fun TarjetaProducto(producto: Producto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LilaClaro)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImagenProducto()
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = producto.precioTexto,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Cuadro lila con la canasta: hace de imagen del producto
@Composable
fun ImagenProducto(tamano: Int = 56) {
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .background(LilaTecsup, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingBasket,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size((tamano / 2).dp)
        )
    }
}
