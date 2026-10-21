package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.tecsup.huellitasvet.ui.components.FilaSwitch
import com.tecsup.huellitasvet.ui.components.OpcionChip
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TituloSeccion
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
            nombre = s.nombre; duracion = s.duracionMin; precio = "%.2f".format(s.precio)
            descripcion = s.descripcion; icono = s.icono; activo = s.activo
            cargado = true
        }
    }

    Scaffold(
        topBar = { AppTopBar(if (servicioId == null) "Nuevo servicio" else "Editar servicio", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VetTextField(nombre, { nombre = it }, "Nombre del servicio", error = errores.nombre)

            TituloSeccion("Duración")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(15, 30, 45, 60).forEach { d ->
                    OpcionChip("$d min", duracion == d, { duracion = d }, Modifier.weight(1f))
                }
            }
            errores.duracion?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetTextField(precio, { precio = it }, "Precio (S/)", error = errores.precio, teclado = KeyboardType.Decimal)
            VetTextField(descripcion, { descripcion = it }, "Descripción", lineas = 2)

            TituloSeccion("Ícono")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("heart", "syringe", "scissors").forEach { i ->
                    Tarjeta(Modifier.weight(1f), onClick = { icono = i }, seleccionada = icono == i) {
                        Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                iconoServicio(i), contentDescription = i,
                                tint = if (icono == i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Tarjeta {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    FilaSwitch("Servicio activo", activo, { activo = it }, "Los dueños podrán pedirlo")
                }
            }

            mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            VetButton("Guardar servicio", onClick = {
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
            VetButton("Cancelar", onClick = onAtras, secundario = true)

            val s = existente
            if (servicioId != null && s != null) {
                TextButton(onClick = { vm.eliminar(s, onListo) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Eliminar servicio", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
