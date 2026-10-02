package com.leon.tecsupstore.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leon.tecsupstore.navigation.Screen

// Lo que se ve DENTRO del menú lateral (va en drawerContent).
// "rutaActual" indica qué opción se resalta.
// "onIrA" avisa a AppNavegacion a qué ruta debe ir (y que cierre el drawer).
@Composable
fun AppDrawer(rutaActual: String?, onIrA: (String) -> Unit) {
    // ModalDrawerSheet: la hoja blanca que sale desde la izquierda
    ModalDrawerSheet(drawerContainerColor = Color.White) {
        Text(
            text = "TECSUP Store",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(8.dp))

        // Destinos principales: el que coincide con la ruta actual sale resaltado
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Inicio") },
            selected = rutaActual == Screen.Home.route,
            onClick = { onIrA(Screen.Home.route) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Mis pedidos") },
            selected = rutaActual == Screen.Pedidos.route,
            onClick = { onIrA(Screen.Pedidos.route) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Favoritos") },
            selected = rutaActual == Screen.Favoritos.route,
            onClick = { onIrA(Screen.Favoritos.route) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Perfil") },
            selected = rutaActual == Screen.Perfil.route,
            onClick = { onIrA(Screen.Perfil.route) }
        )
    }
}
