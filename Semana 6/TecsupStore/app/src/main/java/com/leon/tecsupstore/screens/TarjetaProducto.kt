package com.leon.tecsupstore.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupstore.data.Producto
import com.leon.tecsupstore.ui.theme.LilaClaro
import com.leon.tecsupstore.ui.theme.LilaTecsup

// Tarjeta de un producto: imagen, nombre, precio y el ícono de 3 puntos
@Composable
fun TarjetaProducto(producto: Producto, onClick: () -> Unit) {
    // Estado del menú de esta tarjeta: cada tarjeta tiene el suyo
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        // Con el menú abierto la tarjeta se pone blanca y con borde morado
        colors = CardDefaults.cardColors(containerColor = if (expanded) Color.White else LilaClaro),
        border = if (expanded) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 4.dp),
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
            // Ícono ⋮ arriba a la derecha: por ahora solo cambia el estado
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.align(Alignment.Top)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Mas opciones",
                    modifier = Modifier
                        .background(if (expanded) Color.Transparent else Color.White, CircleShape)
                        .padding(4.dp)
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
