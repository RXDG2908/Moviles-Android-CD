package com.saludplus.citas.ui.screens.doctores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.IconoEspecialidad
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Mis doctores: todos los médicos agrupados por especialidad (groupBy),
// con un buscador por nombre arriba.
@Composable
fun MisDoctoresScreen(navController: NavController) {
    var busqueda by remember { mutableStateOf("") }
    // Cada vez que cambia "busqueda" se vuelven a calcular los grupos
    val grupos = Repositorio.medicosAgrupadosPorEspecialidad(busqueda)

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Mis doctores") { navController.popBackStack() } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Buscador gris con lupa (mismo estilo que Especialidades)
            TextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar médico...", color = TextoGris) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextoGris) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = GrisSuave,
                    unfocusedContainerColor = GrisSuave,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Por cada especialidad: un encabezado y debajo sus médicos
                grupos.forEach { (especialidad, medicos) ->
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconoEspecialidad(especialidad.id, tamano = 36)
                            Spacer(Modifier.width(10.dp))
                            Text(especialidad.nombre, color = TextoOscuro, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    items(medicos) { medico ->
                        TarjetaMedico(medico) {
                            val sede = Repositorio.sedeActual
                            if (sede != null && sede.id in medico.sedes) {
                                // Ya hay sede y el médico atiende ahí: directo a Fecha y hora
                                navController.navigate(Rutas.fechaHora(medico.id))
                            } else {
                                // Primero se elige una de las sedes de ese médico
                                navController.navigate(Rutas.sedes(Rutas.IR_A_FECHA_HORA, medico.id))
                            }
                        }
                    }
                }
            }
        }
    }
}
