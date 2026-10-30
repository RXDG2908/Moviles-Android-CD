package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.model.Horarios
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.AvisoCard
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.veterinario.iconoServicio
import androidx.compose.material.icons.outlined.TouchApp
import com.tecsup.huellitasvet.model.TipoServicio
import com.tecsup.huellitasvet.ui.components.TipoServicioChip
import com.tecsup.huellitasvet.ui.components.VetCombo
import com.tecsup.huellitasvet.ui.components.iconoTipo
import com.tecsup.huellitasvet.ui.theme.colorDeTipo
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel

@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    duenoId: Int,
    mascotaInicial: Int?,
    aceptaCitas: Boolean,
    onCitaCreada: () -> Unit,
    onAtras: () -> Unit,
    tipoInicial: TipoServicio? = null
) {
    LaunchedEffect(duenoId) { vm.iniciar(duenoId, mascotaInicial, tipoInicial) }

    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val serviciosDelTipo by vm.serviciosDelTipo.collectAsState()
    val tipo by vm.tipo.collectAsState()
    val ocupadas by vm.horasOcupadas.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var nota by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar("Pedir cita", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        // Ajuste del veterinario: la clínica puede dejar de recibir citas
        if (!aceptaCitas) {
            AvisoCard(
                icono = Icons.Outlined.EventBusy,
                titulo = "Agenda cerrada",
                texto = "La clínica no está recibiendo citas",
                modifier = Modifier.padding(padding).padding(16.dp)
            )
            return@Scaffold
        }

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------- ¿Quién viene? ----------
            TituloSeccion("¿Quién viene?")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(mascotas, key = { it.id }) { m ->
                    Tarjeta(onClick = { vm.elegirMascota(m.id) }, seleccionada = mascotaId == m.id) {
                        Row(Modifier.padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            MascotaAvatar(Especie.valueOf(m.especie), tamano = 34.dp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                m.nombre,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (mascotaId == m.id) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // ---------- Mejora: tipo de servicio -> servicio en combobox ----------
            TituloSeccion("Tipo de servicio")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoServicio.entries.forEach { t ->
                    TipoServicioChip(
                        tipo = t,
                        seleccionado = tipo == t,
                        onClick = { vm.elegirTipo(t) },
                        modifier = Modifier.weight(1f),
                        cantidad = servicios.count { it.tipo == t.name }
                    )
                }
            }

            val tipoActual = tipo
            if (tipoActual == null) {
                InfoCaja(texto = "Elige un tipo para ver sus servicios", icono = Icons.Outlined.TouchApp)
            } else {
                VetCombo(
                    etiqueta = "Servicio de ${tipoActual.etiqueta.lowercase()}",
                    opciones = serviciosDelTipo,
                    seleccion = serviciosDelTipo.firstOrNull { it.id == servicioId },
                    onSeleccion = { vm.elegirServicio(it.id) },
                    texto = { it.nombre },
                    detalle = { "${it.duracionMin} min · S/ %.2f".format(it.precio) },
                    icono = iconoTipo(tipoActual),
                    iconoOpcion = { Icon(iconoServicio(it.icono), contentDescription = null, tint = colorDeTipo(tipoActual)) },
                    vacio = "No hay servicios activos de este tipo"
                )
                // Detalle del servicio elegido
                serviciosDelTipo.firstOrNull { it.id == servicioId }?.let { s ->
                    Tarjeta(fondo = colorDeTipo(tipoActual).copy(alpha = 0.10f)) {
                        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(iconoServicio(s.icono), contentDescription = null, tint = colorDeTipo(tipoActual))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(s.nombre, style = MaterialTheme.typography.titleSmall)
                                TextoGris(s.descripcion.ifBlank { tipoActual.descripcion })
                                TextoGris("Duración: ${s.duracionMin} min")
                            }
                            Text("S/ %.0f".format(s.precio), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            // ---------- Fecha ----------
            TituloSeccion("Fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(end = 8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(fecha = f, seleccionada = f == fecha, onClick = { vm.elegirFecha(f) })
                }
            }

            // ---------- Hora: 18 horarios en filas de 4 ----------
            Row(verticalAlignment = Alignment.CenterVertically) {
                TituloSeccion("Hora", Modifier.weight(1f))
                TextoGris("Ocupado = tachado")
            }
            Horarios.delDia.chunked(4).forEach { fila ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fila.forEach { h ->
                        HoraChip(
                            hora = h,
                            seleccionada = hora == h,
                            ocupado = h in ocupadas || vm.horaPasada(h), // ocupado o ya pasó
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Relleno para que la última fila no se estire
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            VetTextField(nota, { nota = it }, "Nota (opcional)")

            // ---------- Resumen ----------
            val mascota = mascotas.firstOrNull { it.id == mascotaId }
            val servicio = servicios.firstOrNull { it.id == servicioId }
            if (mascota != null && servicio != null) {
                InfoCaja(
                    titulo = "${mascota.nombre} · ${servicio.nombre}",
                    texto = "${Fechas.larga(fecha.toString())} · ${hora ?: "--:--"} · S/ %.2f".format(servicio.precio)
                )
            }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton(
                "Confirmar cita",
                onClick = { vm.confirmar(nota, onCitaCreada) },
                habilitado = mascota != null && servicio != null && hora != null
            )
        }
    }
}
