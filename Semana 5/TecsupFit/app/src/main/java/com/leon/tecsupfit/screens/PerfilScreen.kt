package com.leon.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mi perfil", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController, Screen.Perfil.route) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta con las iniciales del usuario
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "DR",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Diego Ramos", fontWeight = FontWeight.Bold)
            Text("Plan Premium", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(24.dp))

            // Estadísticas simples
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("14", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Clases")
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("3", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Racha (semanas)")
                    }
                }
            }
        }
    }
}
