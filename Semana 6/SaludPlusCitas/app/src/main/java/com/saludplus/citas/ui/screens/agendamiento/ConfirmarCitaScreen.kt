package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.components.ResumenMedico
import com.saludplus.citas.ui.components.fechaCorta
import com.saludplus.citas.ui.components.rangoHora
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Pantalla 7 - Confirmar cita: usa los 3 parámetros recibidos (medicoId, fecha y hora),
// crea la cita y va a "Cita agendada" borrando el flujo de agendamiento (popUpTo).
@Composable
fun ConfirmarCitaScreen(navController: NavController, medicoId: Int, fecha: String, hora: String) {
    val medico = Repositorio.obtenerMedico(medicoId)
    var motivo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Confirmar cita") { navController.popBackStack() } },
        bottomBar = {
            Column(Modifier.navigationBarsPadding().padding(20.dp)) {
                BotonPrincipal("Agendar cita") {
                    // Crear y guardar la cita (add a la lista)
                    val cita = Repositorio.agendarCita(medicoId, fecha, hora, motivo)
                    if (cita != null) {
                        // popUpTo(HOME): Especialidades, Médicos, Fecha y Confirmar salen del historial
                        navController.navigate(Rutas.citaExitosa(cita.id)) {
                            popUpTo(Rutas.HOME)
                        }
                    } else {
                        error = "Ese horario ya no está disponible, elige otro"
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (medico != null) ResumenMedico(medico, mostrarCmp = true)

            FilaDato(Icons.Default.CalendarMonth, "Fecha", fechaCorta(fecha))
            FilaDato(Icons.Default.Schedule, "Hora", rangoHora(hora))
            FilaDato(Icons.Default.MedicalServices, "Tipo de atención", "Consulta presencial")
            FilaDato(Icons.Default.LocationOn, "Dirección", "Av. Los Olivos 123, Lima")

            // Motivo de consulta (opcional)
            Row {
                Text("Motivo de consulta ", color = TextoOscuro, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("(opcional)", color = TextoGris, fontSize = 14.sp)
            }
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                placeholder = { Text("Consulta de rutina", color = TextoGris) },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = GrisSuave,
                    focusedBorderColor = AzulSalud
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }
        }
    }
}
