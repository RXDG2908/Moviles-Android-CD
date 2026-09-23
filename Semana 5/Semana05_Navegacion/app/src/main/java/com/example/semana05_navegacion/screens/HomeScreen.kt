package com.example.semana05_navegacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.semana05_navegacion.components.OpcionCard
import com.example.semana05_navegacion.navigation.Screen

// Pantalla de bienvenida (reemplaza a la "Pantalla Tecsup")
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, Color(0xFFF3EDF7)))
            )
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(
            text = "Bienvenido,\nJuan León",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("¿Qué deseas gestionar hoy?", color = Color.White.copy(alpha = 0.85f))
        Spacer(modifier = Modifier.height(16.dp))

        OpcionCard(
            icono = Icons.AutoMirrored.Filled.List,
            titulo = "Directorio de Alumnos",
            subtitulo = "Ver y gestionar estudiantes",
            onClick = { navController.navigate(Screen.List.route) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OpcionCard(
            icono = Icons.Default.Person,
            titulo = "Mi Perfil Académico",
            subtitulo = "Datos personales y progreso",
            onClick = { navController.navigate(Screen.Profile.route) }
        )

        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                // Vuelve al login limpiando el historial
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión Segura", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
    }
}
