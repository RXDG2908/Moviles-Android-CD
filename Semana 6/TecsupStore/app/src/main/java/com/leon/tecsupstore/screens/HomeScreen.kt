package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupstore.data.categorias
import com.leon.tecsupstore.data.secciones
import com.leon.tecsupstore.model.Producto
import com.leon.tecsupstore.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("TECSUP Store") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // LazyRow de categorías (filtro)
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
            ) {
                items(categorias) { categoria ->
                    FilterChip(
                        selected = categoria == categoriaSeleccionada,
                        onClick = { categoriaSeleccionada = categoria },
                        label = { Text(categoria) },
                        colors = FilterChipDefaults.filterChipColors()
                    )
                }
            }

            // LazyColumn de secciones, cada una con sus productos filtrados por categoría
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
            ) {
                items(secciones) { seccion ->
                    val productosFiltrados = if (categoriaSeleccionada == "Todos") {
                        seccion.productos
                    } else {
                        seccion.productos.filter { it.categoria == categoriaSeleccionada }
                    }

                    if (productosFiltrados.isNotEmpty()) {
                        Text(
                            text = seccion.titulo,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                        ) {
                            items(productosFiltrados) { producto ->
                                TarjetaProducto(
                                    producto = producto,
                                    onClick = {
                                        navController.navigate(Screen.Detalle.createRoute(producto.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaProducto(producto: Producto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(140.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = producto.nombre, fontWeight = FontWeight.Bold)
            Text(text = "S/ %.2f".format(producto.precio))
        }
    }
}
