package com.leon.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupfit.data.clases
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionScreen(navController: NavController, claseId: Int, horarioIndex: Int) {
    val clase = clases[claseId - 1]
    val horario = clase.horarios[horarioIndex]

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Confirmación") })
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
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "¡Cupo reservado!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(clase.nombre)
            Text("Hoy, $horario · ${clase.sala}")
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    // Limpia Detalle y Confirmación: queda Inicio → Reservas
                    navController.navigate(Screen.Reservas.route) {
                        popUpTo(Screen.Home.route)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver mis reservas")
            }
        }
    }
}
