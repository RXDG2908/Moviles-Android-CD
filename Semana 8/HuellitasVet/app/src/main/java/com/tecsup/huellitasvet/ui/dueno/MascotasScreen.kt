package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.InicioViewModel

@Composable
fun MascotasScreen(
    vm: InicioViewModel,
    duenoId: Int,
    onMascota: (Int) -> Unit,
    onAgregar: () -> Unit
) {
    LaunchedEffect(duenoId) { vm.iniciar(duenoId) }
    val mascotas by vm.mascotas.collectAsState()

    Scaffold(
        topBar = { AppTopBar("Mis mascotas") },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) { Icon(Icons.Filled.Add, contentDescription = "Agregar mascota") }
        },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        if (mascotas.isEmpty()) {
            Text("Aún no registras mascotas", modifier = Modifier.padding(padding).padding(16.dp))
        }
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(mascotas, key = { it.id }) { m ->
                val especie = Especie.valueOf(m.especie)
                Card(
                    onClick = { onMascota(m.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(especie, tamano = 52.dp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(m.nombre, style = MaterialTheme.typography.titleMedium)
                            Text("${especie.etiqueta} · ${m.raza}", style = MaterialTheme.typography.bodyMedium)
                            Text(Fechas.textoEdad(m.anioNacimiento), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }
    }
}
