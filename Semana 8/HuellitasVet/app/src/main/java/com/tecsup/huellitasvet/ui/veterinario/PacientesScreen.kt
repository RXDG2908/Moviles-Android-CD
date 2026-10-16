package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel

@Composable
fun PacientesScreen(vm: PacientesViewModel, onPaciente: (Int) -> Unit) {
    val texto by vm.texto.collectAsState()
    val especie by vm.especie.collectAsState()
    val pacientes by vm.pacientes.collectAsState()
    val visitas by vm.ultimasVisitas.collectAsState()

    // Chips: Todos / Perros / Gatos / Otros
    val filtros = listOf<Pair<String, Especie?>>(
        "Todos" to null, "Perros" to Especie.PERRO, "Gatos" to Especie.GATO, "Otros" to Especie.OTRO
    )

    Column {
        AppTopBar("Pacientes")
        Column(Modifier.padding(horizontal = 16.dp)) {
            VetTextField(texto, vm::buscar, "Buscar por mascota o dueño")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                filtros.forEach { (etiqueta, e) ->
                    FilterChip(selected = especie == e, onClick = { vm.filtrar(e) }, label = { Text(etiqueta) })
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (pacientes.isEmpty()) item { Text("No se encontraron pacientes") }
            items(pacientes, key = { it.mascota.id }) { p ->
                val m = p.mascota
                Card(
                    onClick = { onPaciente(m.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(Especie.valueOf(m.especie), tamano = 52.dp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(m.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("${m.raza} · ${Fechas.textoEdad(m.anioNacimiento)}", style = MaterialTheme.typography.bodyMedium)
                            Text("Dueño: ${p.dueno.nombre}", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Última visita: ${visitas[m.id] ?: "sin visitas"}",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
