package com.leon.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.clinicasalud.data.fechas
import com.leon.clinicasalud.data.horas
import com.leon.clinicasalud.data.medicos
import com.leon.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarScreen(navController: NavController, medicoId: Int) {
    val medico = medicos[medicoId - 1]
    // Fecha y hora elegidas: selección única (-1 = ninguna)
    var fechaElegida by remember { mutableStateOf(-1) }
    var horaElegida by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(medico.nombre, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecciona fecha")
            // Chips que funcionan como RadioButton: solo una fecha
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (index in fechas.indices) {
                    ChipOpcion(
                        texto = fechas[index],
                        seleccionado = fechaElegida == index,
                        onClick = { fechaElegida = index }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecciona hora")
            // Chips que funcionan como RadioButton: solo una hora
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (index in horas.indices) {
                    ChipOpcion(
                        texto = horas[index],
                        seleccionado = horaElegida == index,
                        onClick = { horaElegida = index }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    navController.navigate(
                        Screen.Confirmacion.createRoute(medico.id, fechaElegida, horaElegida)
                    )
                },
                // No deja confirmar sin elegir fecha y hora
                enabled = fechaElegida != -1 && horaElegida != -1,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}
