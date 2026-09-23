package com.example.semana05_navegacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.semana05_navegacion.components.AvatarIniciales
import com.example.semana05_navegacion.components.FilaInfo
import com.example.semana05_navegacion.navigation.Screen

// Pantalla "Configuración de Perfil" (reemplaza a Mi Perfil)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Perfil", fontWeight = FontWeight.Bold) },
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
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Cabecera con degradado horizontal, foto y nombre
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Brush.horizontalGradient(listOf(Color(0xFF5B4A9E), Color(0xFF7A4F63)))),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AvatarIniciales(nombre = "Juan León Suiyon", tamano = 100.dp, conBorde = true)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Juan León Suiyon", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp).weight(1f)) {
                Spacer(modifier = Modifier.height(20.dp))
                TituloSeccion("INFORMACIÓN PERSONAL")
                FilaInfo(Icons.Default.Person, "Nombre Completo", "Juan León Suiyon", conFondo = true)
                FilaInfo(Icons.Default.Email, "Correo", "juan.leon@tecsup.edu.pe", conFondo = true)
                FilaInfo(Icons.Default.Phone, "Teléfono", "+51 987 654 321", conFondo = true)
                Spacer(modifier = Modifier.height(16.dp))
                TituloSeccion("ACADÉMICO")
                FilaInfo(Icons.Default.Info, "Carrera", "Ingeniería de Software", conFondo = true)
                FilaInfo(Icons.Default.DateRange, "Ciclo Actual", "VI Ciclo", conFondo = true)
            }

            Button(
                onClick = {
                    // Vuelve al login limpiando el historial
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF9DEDC),
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Título pequeño de cada sección
@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}
