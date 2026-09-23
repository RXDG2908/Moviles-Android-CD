package com.leon.tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.tecsupfit.data.clases

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(navController: NavController, claseId: Int) {
    // Los id empiezan en 1 y la lista en 0: por eso claseId - 1
    val clase = clases[claseId - 1]
    // Horario elegido: selección única (-1 = ninguno)
    var horarioElegido by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = clase.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${clase.horario} · ${clase.sala} · ${clase.duracion}")
            Spacer(modifier = Modifier.height(12.dp))
            Text(clase.descripcion)
            Spacer(modifier = Modifier.height(12.dp))
            Text(clase.cupos)
            Spacer(modifier = Modifier.height(24.dp))

            Text("Elige un horario", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            // Chips que funcionan como RadioButton: solo uno puede estar elegido
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (index in clase.horarios.indices) {
                    ChipOpcion(
                        texto = clase.horarios[index],
                        seleccionado = horarioElegido == index,
                        onClick = { horarioElegido = index }
                    )
                }
            }
        }
    }
}
