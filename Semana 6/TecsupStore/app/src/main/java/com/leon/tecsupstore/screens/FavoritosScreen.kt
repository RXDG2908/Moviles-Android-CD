package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupstore.data.productos
import com.leon.tecsupstore.navigation.Screen

// Muestra los productos marcados desde el DropdownMenu.
// Usa la misma lista que el badge del menú, así que quitar uno aquí también baja el contador.
@Composable
fun FavoritosScreen(
    navController: NavController,
    favoritos: List<Int>,
    onCambiarFavorito: (Int) -> Unit,
    onAbrirMenu: () -> Unit
) {
    // De la lista general, solo los productos cuyo id está en favoritos
    val productosFavoritos = productos.filter { it.id in favoritos }

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Favoritos",
                subtitulo = "${productosFavoritos.size} productos",
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                    }
                }
            )
        }
    ) { padding ->
        if (productosFavoritos.isEmpty()) {
            // Lista vacía: se explica cómo agregar favoritos
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aun no tienes favoritos. Usa el menu de 3 puntos de un producto para agregarlo.",
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productosFavoritos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        esFavorito = true,
                        onCambiarFavorito = { onCambiarFavorito(producto.id) },
                        onClick = { navController.navigate(Screen.Detalle.createRoute(producto.id)) }
                    )
                }
            }
        }
    }
}
