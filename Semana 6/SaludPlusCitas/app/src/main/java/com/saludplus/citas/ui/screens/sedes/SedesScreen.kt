package com.saludplus.citas.ui.screens.sedes

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.SesionIniciada
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Pantalla de Sedes: antes de agendar (o de ver Mis citas) se elige el local.
// "destino" dice a dónde ir después de elegir; "id" es el dato que necesita ese destino.
@Composable
fun SedesScreen(navController: NavController, destino: String, id: Int) {
    // Sin sesión no se puede agendar: se manda a Login
    if (!SesionIniciada(navController)) return

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Elige tu sede") { navController.popBackStack() } }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("¿En qué sede quieres atenderte?", color = TextoGris, fontSize = 14.sp)
            }
            items(Repositorio.sedes) { sede ->
                TarjetaSede(sede) {
                    // Se guarda la sede y se continúa al destino
                    Repositorio.seleccionarSede(sede.id)
                    when (destino) {
                        Rutas.IR_A_MEDICOS -> navController.navigate(Rutas.medicos(id))
                        else -> navController.navigate(Rutas.ESPECIALIDADES)
                    }
                }
            }
        }
    }
}

// Tarjeta de una sede: ícono de ubicación, nombre, dirección y distrito.
@Composable
private fun TarjetaSede(sede: Sede, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, GrisSuave, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícono de ubicación en cuadro celeste
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AzulClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = AzulSalud)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(sede.nombre, color = TextoOscuro, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(sede.direccion, color = TextoGris, fontSize = 12.sp)
            Text(sede.distrito, color = AzulSalud, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextoGris)
    }
}
