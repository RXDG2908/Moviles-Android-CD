package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.graficos.GraficoBarras
import com.tecsup.huellitasvet.ui.components.graficos.GraficoDona
import com.tecsup.huellitasvet.ui.theme.EstadoAtendida
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente
import com.tecsup.huellitasvet.viewmodel.PanelViewModel

@Composable
fun PanelScreen(vm: PanelViewModel, nombre: String, onVerPendientes: () -> Unit) {
    val hoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    Column(Modifier.verticalScroll(rememberScrollState())) {
        AppTopBar("Panel de la clínica")
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Hola, $nombre", style = MaterialTheme.typography.titleLarge)

            // ---------- 3 contadores de color ----------
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ContadorColor("Hoy", hoy, MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                ContadorColor("Pendientes", pendientes, EstadoPendiente, Modifier.weight(1f))
                ContadorColor("Atendidas", atendidas, EstadoAtendida, Modifier.weight(1f))
            }

            // ---------- Ingresos del mes ----------
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Ingresos del mes", style = MaterialTheme.typography.labelMedium)
                    Text("S/ %.2f".format(ingresos), style = MaterialTheme.typography.headlineSmall)
                }
            }

            // ---------- Alerta de citas por confirmar ----------
            if (pendientes > 0) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = EstadoPendiente.copy(alpha = 0.12f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Tienes $pendientes citas por confirmar", modifier = Modifier.weight(1f))
                        TextButton(onClick = onVerPendientes) { Text("Ver") }
                    }
                }
            }

            Seccion("Citas por estado") { GraficoDona(porEstado) }
            Seccion("Citas por servicio") { GraficoBarras(porServicio) }
        }
    }
}

@Composable
private fun ContadorColor(titulo: String, valor: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), style = MaterialTheme.typography.headlineSmall, color = color)
            Text(titulo, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}
