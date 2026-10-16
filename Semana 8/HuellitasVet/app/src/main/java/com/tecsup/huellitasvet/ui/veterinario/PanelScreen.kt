package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Fechas
import com.tecsup.huellitasvet.ui.components.AvisoCard
import com.tecsup.huellitasvet.ui.components.Tarjeta
import com.tecsup.huellitasvet.ui.components.TextoGris
import com.tecsup.huellitasvet.ui.components.TituloSeccion
import com.tecsup.huellitasvet.ui.components.graficos.GraficoBarras
import com.tecsup.huellitasvet.ui.components.graficos.GraficoDona
import com.tecsup.huellitasvet.ui.theme.EstadoAtendida
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate

@Composable
fun PanelScreen(vm: PanelViewModel, onVerPendientes: () -> Unit) {
    val hoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val atencionesMes by vm.atencionesMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    // Barras: la primera del color del rol y las demás más claras
    val primario = MaterialTheme.colorScheme.primary
    val barras = porServicio.mapIndexed { i, d -> d.copy(color = if (i == 0) primario else primario.copy(alpha = 0.4f)) }

    Column(
        Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // ---------- Encabezado ----------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Panel de la clínica", style = MaterialTheme.typography.headlineSmall)
                TextoGris("Hoy · ${Fechas.larga(LocalDate.now().toString())}")
            }
            Icon(Icons.Filled.Notifications, contentDescription = null)
        }

        // ---------- 3 contadores de color ----------
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ContadorColor("Citas hoy", hoy, primario, Modifier.weight(1f))
            ContadorColor("Pendientes", pendientes, EstadoPendiente, Modifier.weight(1f))
            ContadorColor("Atendidas", atendidas, EstadoAtendida, Modifier.weight(1f))
        }

        // ---------- Ingresos del mes ----------
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = primario, contentColor = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Ingresos del mes", style = MaterialTheme.typography.bodySmall)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("S/ %,.2f".format(ingresos), style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                    Text("$atencionesMes atenciones", style = MaterialTheme.typography.labelLarge)
                }
            }
        }

        // ---------- Gráficos ----------
        TituloSeccion("Citas por estado")
        Tarjeta { GraficoDona(porEstado, Modifier.padding(16.dp)) }

        TituloSeccion("Citas por servicio")
        Tarjeta { GraficoBarras(barras, Modifier.padding(16.dp)) }

        // ---------- Alerta de citas por confirmar ----------
        if (pendientes > 0) {
            AvisoCard(
                icono = Icons.Filled.Warning,
                titulo = "$pendientes citas por confirmar",
                texto = "Revísalas en la agenda",
                accion = {
                    Button(
                        onClick = onVerPendientes,
                        colors = ButtonDefaults.buttonColors(containerColor = EstadoPendiente, contentColor = Color.White)
                    ) { Text("Ver") }
                }
            )
        }
    }
}

@Composable
private fun ContadorColor(titulo: String, valor: Int, color: Color, modifier: Modifier = Modifier) {
    Tarjeta(modifier, fondo = color.copy(alpha = 0.13f)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor.toString(), style = MaterialTheme.typography.headlineSmall, color = color)
            Text(titulo, style = MaterialTheme.typography.labelMedium, color = color)
        }
    }
}
