package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.TarjetaEspecialidad
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.TextoGris

// Pantalla 4 - Especialidades: buscador + LazyColumn que se filtra en tiempo real.
@Composable
fun EspecialidadesScreen(navController: NavController) {
    var busqueda by remember { mutableStateOf("") }
    // Cada vez que cambia "busqueda" se vuelve a calcular la lista (filter + contains)
    val especialidades = Repositorio.buscarEspecialidades(busqueda)

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Especialidades") { navController.popBackStack() } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            // Buscador gris con lupa
            TextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar especialidad...", color = TextoGris) },
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

            // Lista de especialidades
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(especialidades) { especialidad ->
                    TarjetaEspecialidad(especialidad) {
                        navController.navigate(Rutas.medicos(especialidad.id))
                    }
                }
            }
        }
    }
}
