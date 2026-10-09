package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro

// Pantalla 11 - Perfil / Mis datos (vista faltante): datos de la sesión y cerrar sesión.
@Composable
fun PerfilScreen(navController: NavController) {
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    // Iniciales del nombre para el avatar ("Juan Pérez" -> "JP")
    val iniciales = usuario?.nombre
        ?.split(" ")
        ?.filter { it.isNotBlank() }
        ?.take(2)
        ?.joinToString("") { it.first().uppercase() } ?: ""

    Scaffold(
        containerColor = Blanco,
        topBar = { BarraSuperior("Mis datos") { navController.popBackStack() } }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar con iniciales, nombre
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(AzulClaro),
                    contentAlignment = Alignment.Center
                ) {
                    Text(iniciales, color = AzulSalud, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                Text(usuario?.nombre ?: "", color = TextoOscuro, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Paciente", color = TextoGris, fontSize = 13.sp)
            }

            // Datos de la sesión
            FilaDato(Icons.Default.Call, "Teléfono", usuario?.telefono ?: "")
            FilaDato(
                Icons.Default.Email,
                "Correo",
                usuario?.correo?.ifBlank { "No registrado" } ?: ""
            )
            FilaDato(Icons.Default.EventAvailable, "Mis citas", "$totalCitas agendada(s)")

            Spacer(Modifier.weight(1f))

            // Cerrar sesión: vuelve a Splash y limpia todo el historial
            Button(
                onClick = {
                    Repositorio.cerrarSesion()
                    navController.navigate(Rutas.SPLASH) { popUpTo(0) }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.size(8.dp))
                Text("Cerrar sesión", fontWeight = FontWeight.Bold)
            }
        }
    }
}
