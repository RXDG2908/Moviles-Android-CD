package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
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
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val c = cita ?: return@Scaffold
        val mascota = c.mascota.mascota

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ---------- Cabecera ----------
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(Especie.valueOf(mascota.especie), tamano = 64.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(mascota.nombre, style = MaterialTheme.typography.titleLarge)
                        Text("Dueño: ${c.mascota.dueno.nombre}")
                        Text("${c.servicio.nombre} · ${c.cita.hora}")
                    }
                    EstadoCitaChip(EstadoCita.valueOf(c.cita.estado))
                }
            }

            if (!vm.sePuedeAtender(c)) {
                Text("Esta cita ya está ${EstadoCita.valueOf(c.cita.estado).etiqueta.lowercase()}.")
                return@Column
            }

            VetTextField(peso, { peso = it }, "Peso (kg) · anterior: ${mascota.peso} kg",
                error = errorPeso, teclado = KeyboardType.Decimal)
            VetTextField(diagnostico, { diagnostico = it }, "Diagnóstico", error = errorDiagnostico, lineas = 3)
            VetTextField(tratamiento, { tratamiento = it }, "Tratamiento", lineas = 3)

            Text(
                "Se actualizará el peso de ${mascota.nombre} en su ficha",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton("Registrar atención", onClick = {
                vm.registrar(diagnostico, tratamiento, peso.replace(",", ".").toDoubleOrNull() ?: 0.0, onTerminar)
            })
            VetButton("Cancelar cita", onClick = { vm.cancelar(onTerminar) }, secundario = true)
        }
    }
}
