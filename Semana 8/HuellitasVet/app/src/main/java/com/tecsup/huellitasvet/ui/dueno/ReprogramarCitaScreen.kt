package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.AvisoCard
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.ReprogramarCitaViewModel

// El dueño elige otra fecha y hora para una cita PENDIENTE o CONFIRMADA
@Composable
fun ReprogramarCitaScreen(
    vm: ReprogramarCitaViewModel,
    citaId: Int,
    onReprogramada: () -> Unit,
    onAtras: () -> Unit
) {
    LaunchedEffect(citaId) { vm.iniciar(citaId) }

    val cita by vm.cita.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Reprogramar cita", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val c = cita ?: return@Scaffold

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------- Cita actual ----------
            InfoCaja(
                titulo = "${c.mascota.mascota.nombre} · ${c.servicio.nombre}",
                texto = "Horario actual: ${Fechas.larga(c.cita.fecha)} · ${c.cita.hora}"
            )

            if (!vm.puedeReprogramar(c)) {
                AvisoCard(icono = Icons.Outlined.Schedule, texto = "Esta cita ya no se puede reprogramar")
                return@Column
            }

            // ---------- Nueva fecha ----------
            TituloSeccion("Nueva fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(end = 8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(fecha = f, seleccionada = f == fecha, onClick = { vm.elegirFecha(f) })
                }
            }

            // ---------- Nueva hora: 18 horarios en filas de 4 ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                TituloSeccion("Nueva hora", Modifier.weight(1f))
                TextoGris("Ocupado = gris")
            }
            Horarios.delDia.chunked(4).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fila.forEach { h ->
                        HoraChip(
                            hora = h,
                            seleccionada = hora == h,
                            ocupado = h in ocupadas || vm.horaNoDisponible(h), // ocupado o a 2 horas o menos
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            hora?.let {
                InfoCaja(titulo = "Nuevo horario", texto = "${Fechas.larga(fecha.toString())} · $it")
            }

            AvisoCard(
                icono = Icons.Outlined.Schedule,
                texto = "Se puede reprogramar hasta 2 horas antes. La cita vuelve a quedar pendiente hasta que la clínica la confirme."
            )

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton(
                "Guardar nuevo horario",
                onClick = { vm.confirmar(onReprogramada) },
                habilitado = hora != null
            )
        }
    }
}
