package com.leon.tecsupfit.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.leon.tecsupfit.navigation.Screen

// bottomBar con las 4 pestañas.
// "rutaActual" es la pantalla desde donde se llama: así se sabe qué ícono resaltar.
@Composable
fun BarraInferior(navController: NavController, rutaActual: String) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == Screen.Home.route,
            onClick = { irA(navController, Screen.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == Screen.Reservas.route,
            onClick = { irA(navController, Screen.Reservas.route) },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Reservas") },
            label = { Text("Reservas") }
        )
        NavigationBarItem(
            selected = rutaActual == Screen.Rutinas.route,
            onClick = { irA(navController, Screen.Rutinas.route) },
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Rutinas") },
            label = { Text("Rutinas") }
        )
        NavigationBarItem(
            selected = rutaActual == Screen.Perfil.route,
            onClick = { irA(navController, Screen.Perfil.route) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") }
        )
    }
}

// Cambia de pestaña sin apilar pantallas: deja solo Inicio debajo
fun irA(navController: NavController, ruta: String) {
    navController.navigate(ruta) {
        popUpTo(Screen.Home.route)
    }
}
