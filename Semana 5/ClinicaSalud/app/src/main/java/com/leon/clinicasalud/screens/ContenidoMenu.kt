package com.leon.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.leon.clinicasalud.navigation.Screen

// Lo que se ve DENTRO del menú lateral (va en drawerContent).
// "rutaActual" indica qué opción se resalta.
// "onCerrar" avisa a la pantalla que debe cerrar el drawer.
@Composable
fun ContenidoMenu(navController: NavController, rutaActual: String, onCerrar: () -> Unit) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Juan Pérez", fontWeight = FontWeight.Bold)
            Text("Paciente", style = MaterialTheme.typography.bodySmall)
        }
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            selected = rutaActual == Screen.Home.route,
            onClick = {
                onCerrar()
                irA(navController, Screen.Home.route)
            }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("Mis citas") },
            selected = rutaActual == Screen.Citas.route,
            onClick = {
                onCerrar()
                irA(navController, Screen.Citas.route)
            }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = { Text("Historial médico") },
            selected = rutaActual == Screen.Historial.route,
            onClick = {
                onCerrar()
                irA(navController, Screen.Historial.route)
            }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") },
            selected = rutaActual == Screen.Perfil.route,
            onClick = {
                onCerrar()
                irA(navController, Screen.Perfil.route)
            }
        )
    }
}

// Cambia de sección sin apilar pantallas: deja solo Inicio debajo
fun irA(navController: NavController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo(Screen.Home.route)
    }
}
