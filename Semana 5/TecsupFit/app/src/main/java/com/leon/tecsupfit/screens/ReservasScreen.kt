package com.leon.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupfit.data.GestorReservas
import com.leon.tecsupfit.data.Reserva
import com.leon.tecsupfit.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController, gestor: GestorReservas) {
    // Reserva que el usuario quiere cancelar (null = no se muestra el diálogo)
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis reservas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController, Screen.Reservas.route) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Aviso cuando no hay reservas próximas
            if (!gestor.hayConfirmadas()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No tienes reservas próximas", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { irA(navController, Screen.Home.route) }) {
                            Text("Ver clases")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(gestor.reservas) { reserva ->
                    // El color de la tarjeta cambia según el estado
                    val colorFondo =
                        if (reserva.estado == "Confirmada") Color(0xFFDFF3EA) else Color(0xFFEEEEEE)
                    val colorEstado =
                        if (reserva.estado == "Confirmada") Color(0xFF0F6E56) else Color.Gray

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorFondo)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(reserva.clase, fontWeight = FontWeight.Bold)
                                Text(reserva.horario, style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = reserva.estado,
                                    color = colorEstado,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Solo las reservas confirmadas se pueden cancelar
                            if (reserva.estado == "Confirmada") {
                                IconButton(onClick = { reservaACancelar = reserva }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Cancelar reserva",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación antes de cancelar
    val reserva = reservaACancelar
    if (reserva != null) {
        AlertDialog(
            onDismissRequest = { reservaACancelar = null },
            title = { Text("Cancelar reserva") },
            text = { Text("¿Seguro que quieres cancelar tu reserva de ${reserva.clase} (${reserva.horario})?") },
            confirmButton = {
                Button(
                    onClick = {
                        gestor.cancelar(reserva)
                        reservaACancelar = null
                    }
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { reservaACancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}
