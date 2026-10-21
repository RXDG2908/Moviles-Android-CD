package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.InfoCaja
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.EstadoCancelada
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel

@Composable
fun AtenderCitaScreen(vm: AtenderCitaViewModel, citaId: Int, onTerminar: () -> Unit, onAtras: () -> Unit) {
    LaunchedEffect(citaId) { vm.iniciar(citaId) }
    val cita by vm.cita.collectAsState()
    val errorDiagnostico by vm.errorDiagnostico.collectAsState()
    val errorPeso by vm.errorPeso.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var diagnostico by remember { mutableStateOf("") }
    var tratamiento by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    // El campo de peso empieza con el peso anterior de la mascota
    LaunchedEffect(cita?.mascota?.mascota?.id) {
        cita?.let { if (peso.isEmpty()) peso = it.mascota.mascota.peso.toString() }
    }

    Scaffold(
        topBar = { AppTopBar("Atender cita", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val c = cita ?: return@Scaffold
        val mascota = c.mascota.mascota

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------- Cabecera ----------
            Tarjeta {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(Especie.valueOf(mascota.especie), tamano = 64.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("${mascota.nombre} · ${mascota.raza}", style = MaterialTheme.typography.titleSmall)
                        TextoGris("${Fechas.textoEdad(mascota.anioNacimiento)} · ${Sexo.valueOf(mascota.sexo).etiqueta} · Dueño: ${c.mascota.dueno.nombre}")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(iconoServicio(c.servicio.icono), contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "${c.servicio.nombre} · ${Fechas.corta(c.cita.fecha)} · ${c.cita.hora}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            if (!vm.sePuedeAtender(c)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextoGris("Esta cita ya está ")
                    EstadoCitaChip(EstadoCita.valueOf(c.cita.estado))
                }
                return@Column
            }

            TituloSeccion("Registro clínico")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Top) {
                VetTextField(peso, { peso = it }, "Peso (kg)", Modifier.weight(1f), errorPeso, KeyboardType.Decimal)
                // Caja con el peso anterior
                Tarjeta(Modifier.weight(1f).padding(top = 8.dp), fondo = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Favorite, contentDescription = null, tint = EstadoCancelada, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Column {
                            TextoGris("Peso anterior")
                            Text("${mascota.peso} kg", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
            VetTextField(diagnostico, { diagnostico = it }, "Diagnóstico", error = errorDiagnostico, lineas = 3)
            VetTextField(tratamiento, { tratamiento = it }, "Tratamiento", lineas = 3)

            InfoCaja(
                icono = Icons.Filled.Check,
                texto = "Se actualizará el peso de ${mascota.nombre} a ${peso.ifBlank { "-" }} kg"
            )

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton("Registrar atención", onClick = {
                vm.registrar(diagnostico, tratamiento, peso.replace(",", ".").toDoubleOrNull() ?: 0.0, onTerminar)
            })
            VetButton("Cancelar cita", onClick = { vm.cancelar(onTerminar) }, peligro = true)
        }
    }
}
