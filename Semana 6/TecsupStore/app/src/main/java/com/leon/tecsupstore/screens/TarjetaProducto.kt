package com.leon.tecsupstore.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

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
            // El menú va en un Box junto al ícono para que se abra pegado a él
            Box(modifier = Modifier.align(Alignment.Top)) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Mas opciones",
                        modifier = Modifier
                            .background(if (expanded) Color.Transparent else Color.White, CircleShape)
                            .padding(4.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },  // tocar afuera lo cierra
                    // Menú blanco, con esquinas redondeadas y un borde gris fino
                    shape = RoundedCornerShape(12.dp),
                    containerColor = Color.White,
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    DropdownMenuItem(
                        text = { Text("Favoritos") },
                        // leadingIcon: el ícono que va a la izquierda del texto
                        leadingIcon = { IconoMenu(Icons.Default.Favorite) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "${producto.nombre} agregado a favoritos", Toast.LENGTH_SHORT).show()
                        }
                    )
                    // Separador entre una opción y otra
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        leadingIcon = { IconoMenu(Icons.Default.NorthEast) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "Compartiendo ${producto.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        leadingIcon = { IconoMenu(Icons.Outlined.Warning) },
                        onClick = {
                            expanded = false
                            Toast.makeText(context, "${producto.nombre} reportado", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// Ícono pequeño y negro para las opciones del menú
@Composable
fun IconoMenu(icono: ImageVector) {
    Icon(icono, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
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
