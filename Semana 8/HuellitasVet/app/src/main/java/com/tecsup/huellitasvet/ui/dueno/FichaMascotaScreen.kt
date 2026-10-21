package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.tecsup.huellitasvet.model.PuntoPeso
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.SelectorPestanas
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.graficos.GraficoLineas
import com.tecsup.huellitasvet.ui.theme.EstadoConfirmada
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
    val pesos by remember(mascotaId) { vm.pesos(mascotaId) }.collectAsState()
    var pestana by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { AppTopBar(mascota?.nombre ?: "Ficha", onAtras = onAtras) },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0)
    ) { padding ->
        val m = mascota ?: return@Scaffold
        val especie = Especie.valueOf(m.especie)

        Column(
            Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ---------- Cabecera ----------
            Tarjeta {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    MascotaAvatar(especie, tamano = 84.dp)
                    Spacer(Modifier.width(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(m.nombre, style = MaterialTheme.typography.titleLarge)
                        TextoGris("${m.raza} · ${Fechas.textoEdad(m.anioNacimiento)}")
                        TextoGris("${Sexo.valueOf(m.sexo).etiqueta} · ${m.peso} kg")
                        Spacer(Modifier.height(4.dp))
                        // Pequeña píldora verde con el número de atenciones
                        Text(
                            "${historial.size} atenciones",
                            style = MaterialTheme.typography.labelMedium,
                            color = EstadoConfirmada,
                            modifier = Modifier
                                .background(EstadoConfirmada.copy(alpha = 0.14f), RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            if (esDueno) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    VetButton("Pedir cita", onClick = { onPedirCita(m.id) }, modifier = Modifier.weight(1f), icono = Icons.Filled.EventNote)
                    VetButton("Editar", onClick = { onEditar(m.id) }, modifier = Modifier.weight(1f), secundario = true, icono = Icons.Filled.Edit)
                }
            }

            // ---------- Pestañas ----------
            SelectorPestanas(listOf("Datos", "Historial clínico"), pestana, { pestana = it })

            if (pestana == 0) {
                DatosMascota(m)
                GraficoPeso(pesos)
            } else if (historial.isEmpty()) {
                TextoGris("Aún no tiene atenciones")
            } else {
                Column {
                    historial.forEachIndexed { i, c ->
                        HistorialItem(
                            fecha = Fechas.diaMes(c.cita.fecha),
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

// Tarjeta con la evolución del peso en cada atención
@Composable
private fun GraficoPeso(pesos: List<PuntoPeso>) {
    Tarjeta {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Evolución del peso", style = MaterialTheme.typography.titleSmall)
            if (pesos.isEmpty()) {
                TextoGris("Aún no hay pesos registrados en atenciones")
            } else {
                TextoGris("Mín ${pesos.minOf { it.peso }} kg · Máx ${pesos.maxOf { it.peso }} kg · ${pesos.size} registros")
                GraficoLineas(
                    valores = pesos.map { it.peso.toFloat() },
                    etiquetas = pesos.map { Fechas.diaMes(it.fecha) },
                    color = MaterialTheme.colorScheme.primary
                )
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
    Tarjeta {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            filas.forEachIndexed { i, (etiqueta, valor) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    TextoGris(etiqueta, Modifier.weight(1f))
                    Text(valor, style = MaterialTheme.typography.titleSmall)
                }
                if (i < filas.lastIndex) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}
