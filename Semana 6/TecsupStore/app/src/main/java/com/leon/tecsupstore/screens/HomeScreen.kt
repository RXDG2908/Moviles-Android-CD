package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupstore.data.secciones
import com.leon.tecsupstore.navigation.Screen

@Composable
fun HomeScreen(navController: NavController, onAbrirMenu: () -> Unit) {
    // Sección elegida en el LazyRow (empieza en "Mas vendidos")
    var seccionElegida by remember { mutableStateOf(secciones[0]) }

    Scaffold(
        topBar = {
            // El subtítulo muestra la sección que se está viendo
            BarraSuperior(
                titulo = "TECSUP Store",
                subtitulo = seccionElegida.titulo,
                // Ícono ☰: le pide a AppNavegacion que abra el menú lateral
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // LazyRow: una opción por sección
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(secciones) { seccion ->
                    ChipOpcion(
                        texto = seccion.titulo,
                        seleccionado = seccion == seccionElegida,
                        onClick = { seccionElegida = seccion }
                    )
                }
            }

            // LazyColumn: los productos de la sección elegida
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(seccionElegida.productos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onClick = { navController.navigate(Screen.Detalle.createRoute(producto.id)) }
                    )
                }
            }
        }
    }
}
