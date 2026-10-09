package com.saludplus.citas.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 3 - Inicio (diseño)
// TODO: Scaffold con saludo "¡Hola, <nombre>!" (Repositorio.usuarioActual) y campana -> NOTIFICACIONES.
// TODO: 4 tarjetas: Agendar cita -> ESPECIALIDADES, Mis citas -> MIS_CITAS,
//  Mis datos -> PERFIL y Resultados -> RESULTADOS.
// TODO: "Especialidades destacadas" + "Ver todas" y LazyRow con Repositorio.especialidadesDestacadas().
// TODO: bottomBar con NavigationBar de 4 destinos: Inicio, Citas, Resultados, Perfil.
@Composable
fun HomeScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Inicio",
        botones = listOf(
            "Agendar cita" to { navController.navigate(Rutas.ESPECIALIDADES) },
            "Mis citas" to { navController.navigate(Rutas.MIS_CITAS) },
            "Resultados" to { navController.navigate(Rutas.RESULTADOS) },
            "Perfil" to { navController.navigate(Rutas.PERFIL) },
            "Notificaciones" to { navController.navigate(Rutas.NOTIFICACIONES) }
        )
    )
}
