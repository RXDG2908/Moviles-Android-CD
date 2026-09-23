package com.leon.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.clinicasalud.data.Agenda
import com.leon.clinicasalud.data.Cita
import com.leon.clinicasalud.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(navController: NavController, agenda: Agenda) {
    // Cita que el usuario quiere cancelar (null = no se muestra el diálogo)
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ContenidoMenu(
                navController = navController,
                rutaActual = Screen.Citas.route,
                onCerrar = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mis citas", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(agenda.citas) { cita ->
                    // El color de la tarjeta cambia según el estado
                    val colorFondo =
                        if (cita.estado == "Confirmada") Color(0xFFEDE3F6) else Color(0xFFEEEEEE)
                    val colorEstado =
                        if (cita.estado == "Confirmada") Color(0xFF2E7D32) else Color.Gray

                    // Si el menú de tres puntos de esta tarjeta está abierto
                    var expanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorFondo)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(cita.medico, fontWeight = FontWeight.Bold)
                                Text("${cita.fecha}, ${cita.hora}", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cita.estado,
                                    color = colorEstado,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Solo las citas confirmadas tienen menú de opciones
                            if (cita.estado == "Confirmada") {
                                Box {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                                    }
                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text("Cancelar cita", color = MaterialTheme.colorScheme.error)
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            },
                                            onClick = {
                                                expanded = false
                                                citaACancelar = cita
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación antes de cancelar
    val cita = citaACancelar
    if (cita != null) {
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que quieres cancelar tu cita con ${cita.medico} el ${cita.fecha}, ${cita.hora}?") },
            confirmButton = {
                Button(
                    onClick = {
                        agenda.cancelar(cita)
                        citaACancelar = null
                    }
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { citaACancelar = null }) {
                    Text("No")
                }
            }
        )
    }
}
