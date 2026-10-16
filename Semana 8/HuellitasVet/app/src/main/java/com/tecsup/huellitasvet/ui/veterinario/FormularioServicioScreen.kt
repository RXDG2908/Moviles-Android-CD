package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

// Sirve para crear (servicioId = null) y para editar
@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, servicioId: Int?, onListo: () -> Unit, onAtras: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var duracion by remember { mutableIntStateOf(30) }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("heart") }
    var activo by remember { mutableStateOf(true) }

    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    // Modo editar: carga los datos una sola vez
    val existente by remember(servicioId) { vm.servicio(servicioId ?: 0) }.collectAsState()
    var cargado by remember { mutableStateOf(false) }
    LaunchedEffect(existente) {
        val s = existente
        if (s != null && !cargado) {
            nombre = s.nombre; duracion = s.duracionMin; precio = s.precio.toString()
            descripcion = s.descripcion; icono = s.icono; activo = s.activo
            cargado = true
        }
    }

    Scaffold(
        topBar = { AppTopBar(if (servicioId == null) "Nuevo servicio" else "Editar servicio", onAtras = onAtras) },
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VetTextField(nombre, { nombre = it }, "Nombre", error = errores.nombre)

            Text("Duración", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { d ->
                    FilterChip(selected = duracion == d, onClick = { duracion = d }, label = { Text("$d min") })
                }
            }
            errores.duracion?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetTextField(precio, { precio = it }, "Precio (S/)", error = errores.precio, teclado = KeyboardType.Decimal)
            VetTextField(descripcion, { descripcion = it }, "Descripción", lineas = 2)

            Text("Ícono", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("heart", "syringe", "scissors").forEach { i ->
                    FilterChip(
                        selected = icono == i,
                        onClick = { icono = i },
                        label = { Icon(iconoServicio(i), contentDescription = i) }
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Servicio activo", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Switch(checked = activo, onCheckedChange = { activo = it })
            }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton("Guardar", onClick = {
                vm.guardar(
                    Servicio(
                        id = servicioId ?: 0,
                        nombre = nombre,
                        descripcion = descripcion,
                        duracionMin = duracion,
                        precio = precio.replace(",", ".").toDoubleOrNull() ?: 0.0,
                        activo = activo,
                        icono = icono
                    ),
                    alGuardar = onListo
                )
            })

            val s = existente
            if (servicioId != null && s != null) {
                VetButton("Eliminar servicio", onClick = { vm.eliminar(s, onListo) }, secundario = true)
            }
        }
    }
}
