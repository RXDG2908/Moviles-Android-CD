package com.leon.tecsupfit.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavController
import com.leon.tecsupfit.data.rutinas
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rutinas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController, Screen.Rutinas.route) }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(rutinas) { rutina ->
                ListItem(headlineContent = { Text(rutina) })
                HorizontalDivider()
            }
        }
    }
}
