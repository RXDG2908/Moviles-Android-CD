package com.leon.clinicasalud.screens

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
import com.leon.clinicasalud.data.fechas
import com.leon.clinicasalud.data.horas
import com.leon.clinicasalud.data.medicos
import com.leon.clinicasalud.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionScreen(navController: NavController, medicoId: Int, fechaIndex: Int, horaIndex: Int) {
    val medico = medicos[medicoId - 1]

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
                text = "¡Cita agendada!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(medico.nombre)
            Text("${fechas[fechaIndex]}, ${horas[horaIndex]}")
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    // Vuelve al inicio borrando Perfil, Agendar y Confirmación
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al inicio")
            }
        }
    }
}
