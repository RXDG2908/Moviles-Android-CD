package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.IconoEspecialidad
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.Morado
import com.saludplus.citas.ui.theme.MoradoClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeClaro

// Pantalla 3 - Inicio: saludo, 4 accesos, especialidades destacadas
// y barra inferior (NavigationBar) con Inicio, Citas, Resultados y Perfil.
@Composable
fun HomeScreen(navController: NavController) {
    // Solo el primer nombre para el saludo ("Juan Pérez" -> "Juan")
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: ""

    Scaffold(
        containerColor = Blanco,
        bottomBar = { BarraInferior(navController, Rutas.HOME) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Menú y campana
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Menu, contentDescription = null, tint = TextoOscuro)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { navController.navigate(Rutas.NOTIFICACIONES) }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = TextoOscuro)
                }
            }

            // Saludo
            Text("¡Hola, $nombre!", color = TextoOscuro, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("¿Qué deseas hacer hoy?", color = TextoGris, fontSize = 14.sp)
            Spacer(Modifier.height(20.dp))

            // Mosaicos de acceso (2 x 2). "Mis citas" ya no está aquí: se ve desde la
            // pestaña Citas de la barra inferior, después de elegir la sede
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Antes de agendar siempre se elige la sede
                Mosaico("Agendar cita", Icons.Default.CalendarMonth, AzulSalud, AzulClaro, Modifier.weight(1f)) {
                    navController.navigate(Rutas.sedes())
                }
                // En lugar de "Mis citas": todos los médicos por especialidad
                Mosaico("Mis doctores", Icons.Default.MedicalServices, Verde, VerdeClaro, Modifier.weight(1f)) {
                    navController.navigate(Rutas.MIS_DOCTORES)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Mosaico("Mis datos", Icons.Default.Person, Morado, MoradoClaro, Modifier.weight(1f)) {
                    navController.navigate(Rutas.PERFIL)
                }
                Mosaico("Resultados", Icons.Default.Description, Naranja, NaranjaClaro, Modifier.weight(1f)) {
                    navController.navigate(Rutas.RESULTADOS)
                }
            }
            Spacer(Modifier.height(24.dp))

            // Especialidades destacadas
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Especialidades destacadas", color = TextoOscuro, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(
                    "Ver todas",
                    color = AzulSalud,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.navigate(Rutas.sedes()) }
                )
            }
            Spacer(Modifier.height(12.dp))

            // LazyRow con las especialidades destacadas (take)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(end = 4.dp)
            ) {
                items(Repositorio.especialidadesDestacadas()) { especialidad ->
                    // Primero la sede y después los médicos de esa especialidad
                    EspecialidadDestacada(especialidad) {
                        navController.navigate(Rutas.sedes(Rutas.IR_A_MEDICOS, especialidad.id))
                    }
                }
            }
        }
    }
}

// Mosaico de acceso: fondo claro, ícono y texto del mismo color.
@Composable
private fun Mosaico(
    texto: String,
    icono: ImageVector,
    color: Color,
    fondo: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(fondo)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(34.dp))
        Spacer(Modifier.height(10.dp))
        Text(texto, color = color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

// Tarjeta pequeña de una especialidad destacada (ícono arriba, nombre abajo).
@Composable
private fun EspecialidadDestacada(especialidad: Especialidad, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(96.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, GrisSuave, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconoEspecialidad(especialidad.id)
        Spacer(Modifier.height(8.dp))
        Text(
            especialidad.nombre,
            color = TextoOscuro,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}
