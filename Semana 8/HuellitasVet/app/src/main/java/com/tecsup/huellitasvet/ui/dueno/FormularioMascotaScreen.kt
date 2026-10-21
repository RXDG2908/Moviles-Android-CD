package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.tecsup.huellitasvet.ui.components.OpcionChip
import com.tecsup.huellitasvet.ui.components.TituloSeccion
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
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar según la especie elegida, con el botón "+" del mockup
            Box(Modifier.align(Alignment.CenterHorizontally)) {
                MascotaAvatar(Especie.entries.firstOrNull { it.name == especie } ?: Especie.PERRO, tamano = 100.dp)
                Box(
                    Modifier.align(Alignment.BottomEnd).size(30.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                }
            }

            VetTextField(nombre, { nombre = it }, "Nombre", error = errores.nombre)

            TituloSeccion("Especie")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Especie.entries.forEach { e ->
                    OpcionChip(e.etiqueta, especie == e.name, { especie = e.name }, Modifier.weight(1f))
                }
            }
            errores.especie?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

            VetTextField(raza, { raza = it }, "Raza")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                VetTextField(anio, { anio = it }, "Año de nacimiento", Modifier.weight(1f), errores.anio, KeyboardType.Number)
                VetTextField(peso, { peso = it }, "Peso (kg)", Modifier.weight(1f), errores.peso, KeyboardType.Decimal)
            }

            TituloSeccion("Sexo")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Sexo.entries.forEach { s ->
                    OpcionChip(s.etiqueta, sexo == s.name, { sexo = s.name }, Modifier.weight(1f))
                }
            }
            errores.sexo?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton("Guardar mascota", onClick = {
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
            VetButton("Cancelar", onClick = onAtras, secundario = true)

            val m = existente
            if (mascotaId != null && m != null) {
                TextButton(onClick = { vm.eliminar(m, onEliminada) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Eliminar mascota", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
