package com.example.semana05_navegacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.semana05_navegacion.components.AvatarIniciales
import com.example.semana05_navegacion.components.FilaInfo
import com.example.semana05_navegacion.data.alumnos

// Pantalla "Expediente Académico" del alumno elegido (reemplaza al detalle)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    // El itemId llega como Int desde el NavHost: es el id del alumno
    val alumno = alumnos.find { it.id == itemId } ?: alumnos[0]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expediente Académico", fontWeight = FontWeight.Bold) },
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
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabecera con degradado y la foto montada sobre su borde inferior
            Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                        .background(
                            Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, Color(0xFF5A4A7A)))
                        )
                )
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    AvatarIniciales(nombre = alumno.nombre, tamano = 120.dp, conBorde = true)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(alumno.nombre, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(alumno.carrera, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    FilaInfo(Icons.Default.AccountBox, "ID Estudiante", alumno.codigo)
                    FilaInfo(Icons.Default.Email, "Correo Electrónico", alumno.correo)
                    FilaInfo(Icons.Default.Info, "Facultad", alumno.facultad)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    Text("Biografía", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(alumno.biografia, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
