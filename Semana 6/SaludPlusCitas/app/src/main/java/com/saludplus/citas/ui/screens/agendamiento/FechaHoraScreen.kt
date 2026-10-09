package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.ResumenMedico
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Días de la semana (lista fija en la Fase 1): texto del día, número y fecha "yyyy-MM-dd".
private val dias = listOf(
    Triple("Lun", "15", "2026-09-15"),
    Triple("Mar", "16", "2026-09-16"),
    Triple("Mié", "17", "2026-09-17"),
    Triple("Jue", "18", "2026-09-18"),
    Triple("Vie", "19", "2026-09-19")
)

// Pantalla 6 - Fecha y hora: elegir un día y un horario libre del médico.
@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    var fechaElegida by remember { mutableStateOf<String?>(null) }
    var horaElegida by remember { mutableStateOf<String?>(null) }

    // Horarios libres del día elegido. Como "citas" es mutableStateListOf,
    // si alguien reserva un horario, esta lista se vuelve a calcular sola.
    val horarios = fechaElegida?.let { Repositorio.horariosDisponibles(medicoId, it) } ?: emptyList()

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Seleccionar fecha y hora") { navController.popBackStack() } },
        bottomBar = {
            Column(Modifier.navigationBarsPadding().padding(20.dp)) {
                // Continuar solo con día y hora elegidos (y la hora aún libre)
                BotonPrincipal(
                    "Continuar",
                    habilitado = fechaElegida != null && horaElegida != null && horaElegida in horarios
                ) {
                    navController.navigate(Rutas.confirmarCita(medicoId, fechaElegida!!, horaElegida!!))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            if (medico != null) ResumenMedico(medico)
            Spacer(Modifier.height(20.dp))

            // Mes y año
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = TextoOscuro)
                Text(
                    "Setiembre 2026",
                    color = TextoOscuro,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextoOscuro)
            }
            Spacer(Modifier.height(14.dp))

            // Días: el elegido se pinta de azul
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                dias.forEach { (nombreDia, numero, fecha) ->
                    val elegido = fecha == fechaElegida
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (elegido) AzulSalud else Blanco)
                            .clickable { fechaElegida = fecha }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(nombreDia, color = if (elegido) Blanco else TextoGris, fontSize = 12.sp)
                        Text(
                            numero,
                            color = if (elegido) Blanco else TextoOscuro,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            // Horarios disponibles en 3 columnas
            if (fechaElegida == null) {
                Text("Elige un día para ver los horarios", color = TextoGris, fontSize = 13.sp)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(horarios) { hora ->
                        val elegida = hora == horaElegida
                        Text(
                            hora,
                            color = if (elegida) Blanco else TextoOscuro,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (elegida) AzulSalud else GrisSuave)
                                .clickable { horaElegida = hora }
                                .padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
