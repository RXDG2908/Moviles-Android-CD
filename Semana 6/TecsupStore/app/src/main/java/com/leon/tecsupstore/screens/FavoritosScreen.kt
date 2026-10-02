package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FavoritosScreen(onAbrirMenu: () -> Unit) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Favoritos",
                navigationIcon = {
                    IconButton(onClick = onAbrirMenu) {
                        Icon(Icons.Default.Menu, contentDescription = "Abrir menu")
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aqui veras los productos que marques como favoritos",
                textAlign = TextAlign.Center
            )
        }
    }
}
