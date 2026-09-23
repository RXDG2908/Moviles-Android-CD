package com.leon.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupfit.data.Reserva
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController, reservas: List<Reserva>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis reservas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController, Screen.Reservas.route) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reservas) { reserva ->
                // El color de la tarjeta cambia según el estado
                val colorFondo =
                    if (reserva.estado == "Confirmada") Color(0xFFDFF3EA) else Color(0xFFEEEEEE)
                val colorEstado =
                    if (reserva.estado == "Confirmada") Color(0xFF0F6E56) else Color.Gray

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colorFondo)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(reserva.clase, fontWeight = FontWeight.Bold)
                        Text(reserva.horario, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = reserva.estado,
                            color = colorEstado,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
