package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupstore.data.productos

@Composable
fun DetalleProductoScreen(navController: NavController, productoId: Int) {
    // Los id empiezan en 1 y la lista en 0: por eso productoId - 1
    val producto = productos[productoId - 1]

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Detalle del producto",
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagenProducto(tamano = 120)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = producto.precioTexto,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(producto.descripcion, modifier = Modifier.fillMaxWidth())
        }
    }
}
