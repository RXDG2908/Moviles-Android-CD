package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

// 4 ítems para el dueño
val itemsDueno = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
    ItemBarra(Rutas.MIS_CITAS, "Citas", Icons.Filled.Event),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Filled.Person)
)

// 5 ítems para el veterinario
val itemsVet = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Filled.Dashboard),
    ItemBarra(Rutas.AGENDA, "Agenda", Icons.Filled.CalendarMonth),
    ItemBarra(Rutas.PACIENTES, "Pacientes", Icons.Filled.Groups),
    ItemBarra(Rutas.SERVICIOS, "Servicios", Icons.Filled.MedicalServices),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Filled.Settings)
)

@Composable
fun BarraNavegacion(items: List<ItemBarra>, rutaActual: String?, nav: NavHostController) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = {
                    nav.navigate(item.ruta) {
                        // Deja solo la primera pestaña debajo: no se apilan pantallas repetidas
                        popUpTo(items.first().ruta)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(item.icono, contentDescription = item.texto) },
                label = { Text(item.texto) }
            )
        }
    }
}
