package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

data class ItemBarra(val ruta: String, val texto: String, val icono: ImageVector)

// 4 ítems para el dueño
val itemsDueno = listOf(
    ItemBarra(Rutas.INICIO, "Inicio", Icons.Filled.Home),
    ItemBarra(Rutas.MASCOTAS, "Mascotas", Icons.Filled.Pets),
    ItemBarra(Rutas.MIS_CITAS, "Citas", Icons.Filled.EventNote),
    ItemBarra(Rutas.PERFIL, "Perfil", Icons.Outlined.Person)
)

// 5 ítems para el veterinario
val itemsVet = listOf(
    ItemBarra(Rutas.PANEL, "Panel", Icons.Filled.BarChart),
    ItemBarra(Rutas.AGENDA, "Agenda", Icons.Filled.EventNote),
    ItemBarra(Rutas.PACIENTES, "Pacientes", Icons.Filled.Pets),
    ItemBarra(Rutas.SERVICIOS, "Servicios", Icons.Outlined.LocalOffer),
    ItemBarra(Rutas.AJUSTES, "Ajustes", Icons.Outlined.Settings)
)

// Barra blanca con línea superior; el ítem elegido va del color del rol (sin "pastilla" de fondo)
@Composable
fun BarraNavegacion(items: List<ItemBarra>, seleccion: String?, nav: NavHostController) {
    val esquema = MaterialTheme.colorScheme
    Column {
        HorizontalDivider(color = esquema.outlineVariant)
        NavigationBar(containerColor = esquema.surface, tonalElevation = androidx.compose.ui.unit.Dp(0f)) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = seleccion == item.ruta,
                    onClick = {
                        nav.navigate(item.ruta) {
                            // Deja solo la primera pestaña debajo: no se apilan pantallas repetidas
                            popUpTo(items.first().ruta)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(item.icono, contentDescription = item.texto) },
                    label = { Text(item.texto, style = MaterialTheme.typography.labelMedium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = esquema.primary,
                        selectedTextColor = esquema.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = esquema.onSurfaceVariant,
                        unselectedTextColor = esquema.onSurfaceVariant
                    )
                )
            }
        }
    }
}
