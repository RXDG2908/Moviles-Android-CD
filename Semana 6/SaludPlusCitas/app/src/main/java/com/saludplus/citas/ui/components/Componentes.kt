package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.ui.theme.Estrella
import com.saludplus.citas.ui.theme.Verde
import com.saludplus.citas.ui.theme.VerdeChip
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.GrisSuave
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.Rojo
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.TextoOscuro
import androidx.compose.ui.graphics.Color

// Fondo rosado claro de los íconos rojos (Ginecología, Cardiología)
private val RosaClaro = Color(0xFFFCE8EC)

// Componentes reutilizables por crear en este paquete (sugerencia):
// TODO: TarjetaCita(cita, onClick)

// Fila de una especialidad: ícono, nombre, descripción y flecha.
@Composable
fun TarjetaEspecialidad(especialidad: Especialidad, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconoEspecialidad(especialidad.id)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(especialidad.nombre, color = TextoOscuro, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(especialidad.descripcion, color = TextoGris, fontSize = 12.sp)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextoGris)
    }
}

// Avatar circular del médico.
@Composable
fun AvatarMedico(tamano: Int = 56) {
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(CircleShape)
            .background(AzulClaro),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Person, contentDescription = null, tint = AzulSalud, modifier = Modifier.size((tamano * 0.6).dp))
    }
}

// Tarjeta de un médico: avatar, nombre, cargo, calificación y disponibilidad.
@Composable
fun TarjetaMedico(medico: Medico, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, GrisSuave, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarMedico()
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(medico.nombre, color = TextoOscuro, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(medico.cargo, color = TextoGris, fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Estrella, modifier = Modifier.size(16.dp))
                Text(" ${medico.calificacion} (${medico.resenas})", color = TextoGris, fontSize = 12.sp)
            }
            Spacer(Modifier.height(6.dp))
            // Chip de disponibilidad
            Text(
                medico.disponibilidad,
                color = Verde,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(8.dp))
                    .background(VerdeChip)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
}

// Barra inferior (NavigationBar) con los 4 destinos principales.
// "rutaActual" indica qué pestaña se pinta de azul.
@Composable
fun BarraInferior(navController: NavController, rutaActual: String) {
    val destinos = listOf(
        Triple(Rutas.HOME, "Inicio", Icons.Default.Home),
        Triple(Rutas.MIS_CITAS, "Citas", Icons.Default.CalendarMonth),
        Triple(Rutas.RESULTADOS, "Resultados", Icons.Default.Description),
        Triple(Rutas.PERFIL, "Perfil", Icons.Default.Person)
    )
    NavigationBar(containerColor = Blanco) {
        destinos.forEach { (ruta, texto, icono) ->
            NavigationBarItem(
                selected = ruta == rutaActual,
                onClick = {
                    if (ruta != rutaActual) {
                        navController.navigate(ruta) { launchSingleTop = true }
                    }
                },
                icon = { Icon(icono, contentDescription = texto) },
                label = { Text(texto, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulSalud,
                    selectedTextColor = AzulSalud,
                    unselectedIconColor = TextoGris,
                    unselectedTextColor = TextoGris,
                    indicatorColor = Blanco
                )
            )
        }
    }
}

// Barra superior blanca con flecha para volver y el título a su lado.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    titulo: String,
    acciones: @Composable RowScope.() -> Unit = {},
    onVolver: () -> Unit
) {
    TopAppBar(
        title = { Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        },
        actions = acciones,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blanco,
            titleContentColor = TextoOscuro,
            navigationIconContentColor = TextoOscuro
        )
    )
}

// Botón azul de ancho completo con esquinas redondeadas.
@Composable
fun BotonPrincipal(texto: String, habilitado: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AzulSalud),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

// Campo del formulario como en el diseño: ícono en un cuadro celeste a la
// izquierda, y a la derecha la etiqueta en gris sobre el campo de texto.
@Composable
fun CampoFormulario(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    icono: ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    esContrasena: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Cuadro celeste con el ícono
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(AzulClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, contentDescription = null, tint = AzulSalud)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(etiqueta, color = TextoGris, fontSize = 12.sp)
            OutlinedTextField(
                value = valor,
                onValueChange = onCambio,
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = teclado),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = GrisSuave,
                    focusedBorderColor = AzulSalud
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Resumen del médico elegido (fondo gris): avatar, nombre, cargo y opcionalmente el CMP.
@Composable
fun ResumenMedico(medico: Medico, mostrarCmp: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(GrisSuave)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarMedico(52)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(medico.nombre, color = TextoOscuro, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(medico.cargo, color = TextoGris, fontSize = 12.sp)
            if (mostrarCmp) Text("CMP: ${medico.cmp}", color = TextoGris, fontSize = 12.sp)
        }
    }
}

// Ícono de cada especialidad, con su color y fondo claro (como en el diseño).
@Composable
fun IconoEspecialidad(especialidadId: Int, tamano: Int = 44) {
    val (icono, color, fondo) = when (especialidadId) {
        1 -> Triple(Icons.Default.Person, AzulSalud, AzulClaro)          // Medicina General
        2 -> Triple(Icons.Default.ChildCare, Naranja, NaranjaClaro)      // Pediatría
        3 -> Triple(Icons.Default.Female, Rojo, RosaClaro)               // Ginecología
        4 -> Triple(Icons.Default.Favorite, Rojo, RosaClaro)             // Cardiología
        5 -> Triple(Icons.Default.Spa, Naranja, NaranjaClaro)            // Dermatología
        6 -> Triple(Icons.Default.Healing, AzulSalud, AzulClaro)         // Traumatología
        else -> Triple(Icons.Default.Visibility, AzulSalud, AzulClaro)   // Oftalmología
    }
    Box(
        modifier = Modifier
            .size(tamano.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Icon(icono, contentDescription = null, tint = color)
    }
}

// Contenido temporal de las pantallas que aún no están terminadas.
// Al completar una pantalla, borra la llamada a PantallaEnConstruccion.
@Composable
fun PantallaEnConstruccion(
    titulo: String,
    botones: List<Pair<String, () -> Unit>> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Pantalla en construcción")
        botones.forEach { (texto, accion) ->
            Button(onClick = accion, modifier = Modifier.fillMaxWidth()) {
                Text(texto)
            }
        }
    }
}
