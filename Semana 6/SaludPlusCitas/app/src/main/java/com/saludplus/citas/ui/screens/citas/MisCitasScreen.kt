package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.components.TarjetaCita
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Pantalla 10 - Mis citas (vista faltante): LazyColumn con las citas del usuario
// y mensaje cuando la lista está vacía.
@Composable
fun MisCitasScreen(navController: NavController) {
    // citas es mutableStateListOf: si se agrega o quita una, la lista se actualiza sola
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Mis citas") { navController.popBackStack() } }
    ) { padding ->
        if (citas.isEmpty()) {
            // Lista vacía
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.EventBusy, contentDescription = null, tint = TextoGris, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Aún no tienes citas", color = TextoOscuro, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "Agenda tu primera cita con uno de nuestros especialistas",
                        color = TextoGris,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    BotonPrincipal("Agendar cita") { navController.navigate(Rutas.sedes()) }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas) { cita ->
                    TarjetaCita(cita) { navController.navigate(Rutas.detalleCita(cita.id)) }
                }
            }
        }
    }
}
