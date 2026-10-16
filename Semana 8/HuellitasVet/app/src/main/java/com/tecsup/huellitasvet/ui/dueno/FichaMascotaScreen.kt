package com.tecsup.huellitasvet.ui.dueno

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

// Ficha de la mascota. El veterinario la ve sin los botones del dueño (esDueno = false)
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    mascotaId: Int,
    esDueno: Boolean,
    onPedirCita: (Int) -> Unit,
    onEditar: (Int) -> Unit,
    onAtras: () -> Unit
) {
    val mascota by remember(mascotaId) { vm.mascota(mascotaId) }.collectAsState()
    val historial by remember(mascotaId) { vm.historial(mascotaId) }.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { AppTopBar("Ficha", onAtras = onAtras) },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val m = mascota ?: return@Scaffold
        val especie = Especie.valueOf(m.especie)

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ---------- Cabecera ----------
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(especie, tamano = 84.dp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(m.nombre, style = MaterialTheme.typography.headlineSmall)
                        Text("${especie.etiqueta} · ${m.raza}")
                        Text("${Fechas.textoEdad(m.anioNacimiento)} · ${m.peso} kg")
                    }
                }
            }

            if (esDueno) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    VetButton("Pedir cita", onClick = { onPedirCita(m.id) }, modifier = Modifier.weight(1f))
                    VetButton("Editar", onClick = { onEditar(m.id) }, modifier = Modifier.weight(1f), secundario = true)
                }
            }

            // ---------- Pestañas ----------
            TabRow(selectedTabIndex = pestana) {
                Tab(selected = pestana == 0, onClick = { pestana = 0 }, text = { Text("Datos") })
                Tab(selected = pestana == 1, onClick = { pestana = 1 }, text = { Text("Historial clínico") })
            }

            if (pestana == 0) {
                DatosMascota(m)
            } else if (historial.isEmpty()) {
                Text("Aún no tiene atenciones", style = MaterialTheme.typography.bodyLarge)
            } else {
                Column {
                    historial.forEachIndexed { i, c ->
                        HistorialItem(
                            fecha = c.cita.fecha,
                            servicio = c.servicio.nombre,
                            diagnostico = c.cita.diagnostico,
                            tratamiento = c.cita.tratamiento,
                            peso = c.cita.pesoRegistrado,
                            esUltimo = i == historial.lastIndex
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DatosMascota(m: Mascota) {
    val filas = listOf(
        "Especie" to Especie.valueOf(m.especie).etiqueta,
        "Raza" to m.raza.ifBlank { "-" },
        "Sexo" to Sexo.valueOf(m.sexo).etiqueta,
        "Año de nacimiento" to m.anioNacimiento.toString(),
        "Peso actual" to "${m.peso} kg"
    )
    Column {
        filas.forEach { (etiqueta, valor) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                Text(etiqueta, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                Text(valor, style = MaterialTheme.typography.titleMedium)
            }
            HorizontalDivider()
        }
    }
}
