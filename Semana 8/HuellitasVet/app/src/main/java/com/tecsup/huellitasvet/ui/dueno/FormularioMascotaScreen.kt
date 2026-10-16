package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
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
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

// Sirve para crear (mascotaId = null) y para editar (mascotaId = id)
@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    duenoId: Int,
    mascotaId: Int?,
    onListo: () -> Unit,
    onEliminada: () -> Unit,
    onAtras: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    // Modo editar: carga los datos una sola vez
    val existente by remember(mascotaId) { vm.mascota(mascotaId ?: 0) }.collectAsState()
    var cargado by remember { mutableStateOf(false) }
    LaunchedEffect(existente) {
        val m = existente
        if (m != null && !cargado) {
            nombre = m.nombre; raza = m.raza; anio = m.anioNacimiento.toString()
            peso = m.peso.toString(); especie = m.especie; sexo = m.sexo
            cargado = true
        }
    }

    Scaffold(
        topBar = { AppTopBar(if (mascotaId == null) "Nueva mascota" else "Editar mascota", onAtras = onAtras) },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar según la especie elegida
            MascotaAvatar(
                Especie.entries.firstOrNull { it.name == especie } ?: Especie.OTRO,
                tamano = 96.dp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            VetTextField(nombre, { nombre = it }, "Nombre", error = errores.nombre)

            Text("Especie", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Especie.entries.forEach { e ->
                    FilterChip(selected = especie == e.name, onClick = { especie = e.name }, label = { Text(e.etiqueta) })
                }
            }
            errores.especie?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetTextField(raza, { raza = it }, "Raza")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetTextField(anio, { anio = it }, "Año de nacimiento", Modifier.weight(1f), errores.anio, KeyboardType.Number)
                VetTextField(peso, { peso = it }, "Peso (kg)", Modifier.weight(1f), errores.peso, KeyboardType.Decimal)
            }

            Text("Sexo", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Sexo.entries.forEach { s ->
                    FilterChip(selected = sexo == s.name, onClick = { sexo = s.name }, label = { Text(s.etiqueta) })
                }
            }
            errores.sexo?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton("Guardar", onClick = {
                vm.guardar(
                    Mascota(
                        id = mascotaId ?: 0,
                        duenoId = duenoId,
                        nombre = nombre,
                        especie = especie,
                        raza = raza,
                        anioNacimiento = anio.toIntOrNull() ?: 0,
                        peso = peso.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        sexo = sexo
                    ),
                    alGuardar = onListo
                )
            })

            val m = existente
            if (mascotaId != null && m != null) {
                VetButton("Eliminar mascota", onClick = { vm.eliminar(m, onEliminada) }, secundario = true,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
