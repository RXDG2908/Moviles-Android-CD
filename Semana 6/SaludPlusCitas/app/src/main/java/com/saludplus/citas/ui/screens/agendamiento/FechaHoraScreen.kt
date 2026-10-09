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
import androidx.compose.material3.IconButton
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
import com.saludplus.citas.ui.components.SesionIniciada
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.ResumenMedico
import com.saludplus.citas.ui.components.diasDeAtencion
import com.saludplus.citas.ui.components.diasHabiles
import com.saludplus.citas.ui.components.mesYAnio
import com.saludplus.citas.ui.components.nombreDiaCorto
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro
import androidx.compose.ui.text.style.TextDecoration
import com.saludplus.citas.ui.components.Leyenda
import com.saludplus.citas.ui.components.coloresCupos
import com.saludplus.citas.ui.theme.Amarillo
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeClaro
import java.time.LocalDate

// Pantalla 6 - Fecha y hora: elegir un día y un horario libre del médico.
@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    // Sin sesión no se puede agendar: se manda a Login
    if (!SesionIniciada(navController)) return

    val medico = Repositorio.obtenerMedico(medicoId)
    var fechaElegida by remember { mutableStateOf<String?>(null) }
    var horaElegida by remember { mutableStateOf<String?>(null) }

    // Calendario dinámico: 5 días hábiles (sin sábados, domingos ni días pasados).
    // semana = 0 es la semana actual (desde hoy); cada flecha suma o resta 1.
    // Cada día se guarda como "yyyy-MM-dd", igual que en Cita.fecha
    val hoy = remember { LocalDate.now() }
    var semana by remember { mutableStateOf(0) }
    val dias = diasHabiles(hoy.plusWeeks(semana.toLong()))
    // ¿Queda al menos un día con cupos en esta semana?
    val hayCupos = dias.any { Repositorio.cuposLibres(medicoId, it) > 0 }

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
            if (medico != null) {
                ResumenMedico(medico)
                Spacer(Modifier.height(8.dp))
                Text("Atiende: ${diasDeAtencion(medico.diasAtencion)}", color = TextoGris, fontSize = 13.sp)
            }
            Spacer(Modifier.height(20.dp))

            // Mes y año con flechas para cambiar de semana
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                // < retrocede una semana; no se puede retroceder antes de la semana actual
                // Al cambiar de semana se borran el día y la hora elegidos: el día anterior
                // ya no se ve y no se debe poder continuar con él
                IconButton(
                    onClick = {
                        semana--
                        fechaElegida = null
                        horaElegida = null
                    },
                    enabled = semana > 0
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null, tint = TextoOscuro)
                }
                // El mes y el año salen del primer día mostrado: cambian solos con la semana
                Text(
                    mesYAnio(dias[0]),
                    color = TextoOscuro,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                // > avanza una semana
                IconButton(
                    onClick = {
                        semana++
                        fechaElegida = null
                        horaElegida = null
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextoOscuro)
                }
            }
            Spacer(Modifier.height(14.dp))

            // Días como butacas de cine: todos se ven, pero solo se pueden tocar los que
            // tienen cupos. El color dice cuántos quedan; el elegido se pinta de azul.
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                dias.forEach { dia ->
                    val fecha = dia.toString()          // LocalDate -> "2026-10-12"
                    val elegido = fecha == fechaElegida
                    val atiende = Repositorio.atiende(medicoId, dia)
                    val libres = Repositorio.cuposLibres(medicoId, dia)
                    val bloqueado = libres == 0
                    val (colorCupos, fondoCupos) = coloresCupos(libres)

                    // Colores según el estado del día
                    val fondo = when {
                        elegido -> AzulSalud
                        bloqueado -> GrisSuave
                        else -> fondoCupos
                    }
                    val colorTexto = when {
                        elegido -> Blanco
                        bloqueado -> TextoGris
                        else -> TextoOscuro
                    }
                    // Debajo del número: cupos libres o por qué está bloqueado
                    val detalle = when {
                        !atiende -> "No atiende"
                        bloqueado -> "Lleno"
                        libres == 1 -> "1 cupo"
                        else -> "$libres cupos"
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(fondo)
                            // Un día bloqueado no se puede elegir
                            .clickable(enabled = !bloqueado) {
                                // Al cambiar de día la hora se reinicia y los horarios se recalculan
                                fechaElegida = fecha
                                horaElegida = null
                            }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(nombreDiaCorto(dia), color = if (elegido) Blanco else TextoGris, fontSize = 12.sp)
                        Text(
                            dia.dayOfMonth.toString(),
                            color = colorTexto,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (bloqueado) TextDecoration.LineThrough else null
                        )
                        Text(
                            detalle,
                            color = when {
                                elegido -> Blanco
                                bloqueado -> TextoGris
                                else -> colorCupos
                            },
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Leyenda(
                listOf(
                    Verde to "Muchos cupos",
                    Amarillo to "Pocos",
                    Rojo to "Últimos",
                    TextoGris to "Bloqueado"
                )
            )
            Spacer(Modifier.height(16.dp))

            // Horarios del día elegido en 3 columnas: los ocupados o ya pasados se ven
            // tachados y en gris (como butacas vendidas) y no se pueden tocar
            if (!hayCupos) {
                Text(
                    "El médico no tiene cupos esta semana. Prueba con la semana siguiente.",
                    color = TextoGris,
                    fontSize = 13.sp
                )
            } else if (fechaElegida == null) {
                Text("Elige un día para ver los horarios", color = TextoGris, fontSize = 13.sp)
            } else {
                Leyenda(
                    listOf(
                        Verde to "Libre",
                        TextoGris to "Ocupado",
                        AzulSalud to "Elegido"
                    )
                )
                Spacer(Modifier.height(10.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(Repositorio.horariosBase) { hora ->
                        val elegida = hora == horaElegida
                        val libre = hora in horarios
                        Text(
                            hora,
                            color = when {
                                elegida -> Blanco
                                libre -> Verde
                                else -> TextoGris
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            textDecoration = if (libre) null else TextDecoration.LineThrough,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        elegida -> AzulSalud
                                        libre -> VerdeClaro
                                        else -> GrisSuave
                                    }
                                )
                                .clickable(enabled = libre) { horaElegida = hora }
                                .padding(vertical = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
