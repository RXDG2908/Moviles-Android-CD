package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeChip

// Pantalla 9 - Cita agendada (vista faltante, mismo estilo que el diseño):
// mensaje de éxito y resumen de la cita recién creada.
@Composable
fun CitaExitosaScreen(navController: NavController, citaId: Int) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let { Repositorio.obtenerMedico(it.medicoId) }
    val sede = cita?.let { Repositorio.obtenerSede(it.sedeId) }

    // Volver al Inicio (ya está en el historial gracias a popUpTo)
    val irAlInicio = { navController.popBackStack(Rutas.HOME, inclusive = false) }

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Cita agendada") { irAlInicio() } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            // Check verde de éxito
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(VerdeChip),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Verde, modifier = Modifier.size(48.dp))
            }
            Text("¡Cita agendada!", color = TextoOscuro, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(
                "Tu cita fue registrada correctamente. Te esperamos.",
                color = TextoGris,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            // Resumen de la cita
            if (cita != null && medico != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(GrisSuave)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResumenMedico(medico)
                    FilaDato(Icons.Default.CalendarMonth, "Fecha", fechaCorta(cita.fecha))
                    FilaDato(Icons.Default.Schedule, "Hora", rangoHora(cita.hora))
                    if (sede != null) {
                        FilaDato(Icons.Default.LocationOn, "Sede", "${sede.nombre} - ${sede.direccion}")
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            BotonPrincipal("Ver mis citas") {
                navController.navigate(Rutas.MIS_CITAS) { popUpTo(Rutas.HOME) }
            }
            OutlinedButton(
                onClick = { irAlInicio() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Ir al inicio", color = AzulSalud, fontWeight = FontWeight.Bold)
            }
        }
    }
}
