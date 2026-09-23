package com.leon.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.clinicasalud.data.Medico
import com.leon.clinicasalud.data.medicos
import com.leon.clinicasalud.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")
    // Especialidad elegida en el LazyRow
    var especialidadElegida by remember { mutableStateOf("Todas") }

    // Estado del menú lateral y scope para abrirlo/cerrarlo
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // El drawer ENVUELVE al Scaffold: vive afuera, no es un parámetro de Scaffold
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ContenidoMenu(
                navController = navController,
                rutaActual = Screen.Home.route,
                onCerrar = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("Clínica Salud+", fontWeight = FontWeight.Bold)
                            Text("Hola, Juan", style = MaterialTheme.typography.bodySmall)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // LazyRow: chips de especialidad
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(especialidades) { especialidad ->
                        ChipOpcion(
                            texto = especialidad,
                            seleccionado = especialidad == especialidadElegida,
                            onClick = { especialidadElegida = especialidad }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Médicos disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // LazyColumn: lista de médicos (solo los de la especialidad elegida)
                LazyColumn {
                    items(medicos) { medico ->
                        if (especialidadElegida == "Todas" || medico.especialidad == especialidadElegida) {
                            TarjetaMedico(medico = medico)
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta de un médico: nombre, especialidad y calificación
@Composable
fun TarjetaMedico(medico: Medico) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(medico.nombre, fontWeight = FontWeight.Bold)
                Text(medico.especialidad, style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFE0A800)
            )
            Text("${medico.calificacion}")
        }
    }
}
