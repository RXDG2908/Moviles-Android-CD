package com.leon.tecsupfit.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupfit.data.ClaseGimnasio
import com.leon.tecsupfit.data.clases
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val filtros = listOf("Hoy", "Esta semana")
    // Filtro elegido en el LazyRow
    var filtroElegido by remember { mutableStateOf("Hoy") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Fit", fontWeight = FontWeight.Bold)
                        Text("Hola, Diego", style = MaterialTheme.typography.bodySmall)
                    }
                }
            )
        },
        bottomBar = { BarraInferior(navController, Screen.Home.route) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // LazyRow: chips de filtro
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtros) { filtro ->
                    ChipOpcion(
                        texto = filtro,
                        seleccionado = filtro == filtroElegido,
                        onClick = { filtroElegido = filtro }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Clases disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn: lista de clases (solo se dibujan las del filtro elegido)
            LazyColumn {
                items(clases) { clase ->
                    if (clase.dia == filtroElegido) {
                        TarjetaClase(
                            clase = clase,
                            onClick = {
                                navController.navigate(Screen.Detalle.createRoute(clase.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta de una clase: nombre y horario
@Composable
fun TarjetaClase(clase: ClaseGimnasio, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(clase.nombre, fontWeight = FontWeight.Bold)
                Text(
                    text = "${clase.horario} · ${clase.sala} · ${clase.tipo}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
