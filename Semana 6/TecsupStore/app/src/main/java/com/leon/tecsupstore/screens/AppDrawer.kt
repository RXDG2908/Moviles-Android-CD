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

// Lo que se ve DENTRO del menú lateral (va en drawerContent).
// "onCerrar" avisa a AppNavegacion que debe cerrar el drawer.
@Composable
fun AppDrawer(onCerrar: () -> Unit) {
    // ModalDrawerSheet: la hoja blanca que sale desde la izquierda
    ModalDrawerSheet(drawerContainerColor = Color.White) {
        Text(
            text = "TECSUP Store",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(8.dp))

        // Destinos principales: por ahora solo cierran el menú
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Inicio") },
            selected = false,
            onClick = onCerrar
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Mis pedidos") },
            selected = false,
            onClick = onCerrar
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Favoritos") },
            selected = false,
            onClick = onCerrar
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
            label = { Text("Perfil") },
            selected = false,
            onClick = onCerrar
        )
    }
}
