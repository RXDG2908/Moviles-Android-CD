package com.leon.tecsupstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leon.tecsupstore.data.usuario
import com.leon.tecsupstore.navigation.Screen
import com.leon.tecsupstore.ui.theme.LilaTecsup

// Lo que se ve DENTRO del menú lateral (va en drawerContent).
// "rutaActual" indica qué opción se resalta.
// "onIrA" avisa a AppNavegacion a qué ruta debe ir (y que cierre el drawer).
// "onCerrarSesion" avisa que se tocó Cerrar sesion.
// "cantidadFavoritos" es cuántos productos se marcaron desde el DropdownMenu.
@Composable
fun AppDrawer(
    rutaActual: String?,
    cantidadFavoritos: Int,
    onIrA: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    // ModalDrawerSheet: la hoja blanca que sale desde la izquierda
    ModalDrawerSheet(drawerContainerColor = Color.White) {
        // Encabezado: avatar con iniciales, nombre y correo del usuario
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(iniciales = usuario.iniciales, tamano = 56.dp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(usuario.correo, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
            }
        }
        HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(16.dp))

        // Destinos principales: el que coincide con la ruta actual sale resaltado
        ItemMenu("Inicio", rutaActual == Screen.Home.route) { onIrA(Screen.Home.route) }
        ItemMenu("Mis pedidos", rutaActual == Screen.Pedidos.route) { onIrA(Screen.Pedidos.route) }
        // Favoritos lleva el badge con el contador
        ItemMenu("Favoritos", rutaActual == Screen.Favoritos.route, contador = cantidadFavoritos) {
            onIrA(Screen.Favoritos.route)
        }
        ItemMenu("Perfil", rutaActual == Screen.Perfil.route) { onIrA(Screen.Perfil.route) }
        // Cerrar sesion nunca queda resaltado porque no es una pantalla
        ItemMenu("Cerrar sesion", false, onClick = onCerrarSesion)
    }
}

// Una opción del menú: la elegida tiene fondo lila y texto morado en negrita,
// el resto queda en blanco con texto negro.
// "contador" es opcional: si es mayor que 0 se dibuja un badge morado a la derecha.
@Composable
fun ItemMenu(texto: String, seleccionado: Boolean, contador: Int = 0, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.RadioButtonUnchecked, contentDescription = null) },
        label = { Text(texto, fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal) },
        selected = seleccionado,
        onClick = onClick,
        // badge: lo que va a la derecha del texto; con 0 favoritos no se muestra nada
        badge = {
            if (contador > 0) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Text("$contador")
                }
            }
        },
        shape = RoundedCornerShape(12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = LilaTecsup,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            selectedIconColor = Color.Black,
            unselectedContainerColor = Color.White,
            unselectedTextColor = Color.Black,
            unselectedIconColor = Color.Black
        ),
        // Margen a los lados para que el resaltado no toque los bordes
        modifier = Modifier
            .padding(NavigationDrawerItemDefaults.ItemPadding)
            .height(64.dp)
    )
}

// Círculo lila con las iniciales en morado (se usa en el menú y en el Perfil)
@Composable
fun Avatar(iniciales: String, tamano: Dp) {
    Box(
        modifier = Modifier
            .size(tamano)
            .background(LilaTecsup, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iniciales,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = (tamano.value / 2.8).sp
        )
    }
}
