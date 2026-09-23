package com.leon.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis reservas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController, Screen.Reservas.route) }
    ) { padding ->
        // Mensaje centrado mientras todavía no hay reservas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Aún no tienes reservas",
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
