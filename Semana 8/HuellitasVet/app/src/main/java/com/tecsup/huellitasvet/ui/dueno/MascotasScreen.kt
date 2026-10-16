package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
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
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
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
            FloatingActionButton(
                onClick = onAgregar,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(20.dp)
            ) { Icon(Icons.Filled.Add, contentDescription = "Agregar mascota") }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (mascotas.isEmpty()) item { TextoGris("Aún no registras mascotas") }
            items(mascotas, key = { it.id }) { m ->
                val especie = Especie.valueOf(m.especie)
                Tarjeta(onClick = { onMascota(m.id) }) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(especie, tamano = 56.dp)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(m.nombre, style = MaterialTheme.typography.titleSmall)
                            TextoGris("${especie.etiqueta} · ${m.raza} · ${Fechas.textoEdad(m.anioNacimiento)}")
                            TextoGris("${Sexo.valueOf(m.sexo).etiqueta} · ${m.peso} kg")
                        }
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
