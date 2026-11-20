// Pantalla principal: botón para buscar la ubicación y el resultado.
package com.leon.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.campusgps.model.Lugar
import com.leon.campusgps.model.UbicacionUi
import com.leon.campusgps.util.formatearDistancia
import com.leon.campusgps.viewmodel.MapaViewModel
import java.util.Locale

private const val FINE = Manifest.permission.ACCESS_FINE_LOCATION
private const val COARSE = Manifest.permission.ACCESS_COARSE_LOCATION
private val VerdeCercano = Color(0xFF2E7D32)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugares.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercano.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    val radio by vm.radio.collectAsStateWithLifecycle()
    val dentroDelRadio by vm.dentroDelRadio.collectAsStateWithLifecycle()
    val siguiendo by vm.siguiendo.collectAsStateWithLifecycle()
    val ruta by vm.ruta.collectAsStateWithLifecycle()
    // Recuerda qué pidió el usuario mientras Android muestra el cuadro de permiso
    var quiereSeguir by remember { mutableStateOf(false) }
    // Solo se busca la distancia ya calculada para mostrarla, no se calcula aquí
    val metrosCercano = lugares.firstOrNull { it.lugar.id == masCercano?.id }?.metros

    // Mapa en pantalla completa: se oculta todo lo demás (se conserva al rotar)
    var pantallaCompleta by rememberSaveable { mutableStateOf(false) }
    // El botón Atrás primero sale de pantalla completa
    BackHandler(enabled = pantallaCompleta) { pantallaCompleta = false }
    val margenH by animateDpAsState(if (pantallaCompleta) 0.dp else 16.dp, tween(400), label = "margenH")
    val margenV by animateDpAsState(if (pantallaCompleta) 0.dp else 10.dp, tween(400), label = "margenV")
    val entrar = expandVertically(tween(400)) + fadeIn(tween(300))
    val salir = shrinkVertically(tween(400)) + fadeOut(tween(200))

    val contexto = LocalContext.current
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        // Basta con que acepte uno de los dos (precisa o aproximada)
        when {
            !resultado.values.any { it } -> vm.permisoDenegado()
            quiereSeguir -> vm.iniciarSeguimiento()
            else -> vm.buscar()
        }
    }

    // Abre la pantalla de permisos de la app en los Ajustes de Android
    fun abrirAjustes() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", contexto.packageName, null)
        )
        contexto.startActivity(intent)
    }

    fun tienePermiso() = listOf(FINE, COARSE).any {
        ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED
    }

    fun alPulsar() {
        quiereSeguir = false
        if (tienePermiso()) vm.buscar() else lanzador.launch(arrayOf(FINE, COARSE))
    }

    fun alPulsarSeguir() {
        if (siguiendo) {
            vm.detenerSeguimiento()
            return
        }
        quiereSeguir = true
        if (tienePermiso()) vm.iniciarSeguimiento() else lanzador.launch(arrayOf(FINE, COARSE))
    }

    Scaffold(
        topBar = {
            AnimatedVisibility(visible = !pantallaCompleta, enter = entrar, exit = salir) {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Explore, contentDescription = null)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("CampusGPS", fontWeight = FontWeight.Bold)
                            Text(
                                "Lugares del campus cerca de ti",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            }
        }
    ) { padding ->
        // Sin spacedBy: cada bloque lleva su propio espacio para que, al ocultarse
        // en pantalla completa, no quede ningún hueco alrededor del mapa
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = margenH, vertical = margenV),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = !pantallaCompleta, enter = entrar, exit = salir) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    PanelUbicacion(
                        estado = estado,
                        siguiendo = siguiendo,
                        onBuscar = { alPulsar() },
                        onSeguir = { alPulsarSeguir() },
                        onAbrirAjustes = { abrirAjustes() }
                    )

                    // Chips para ver solo una categoría en el mapa y en la lista
                    FiltroCategorias(
                        categorias = vm.categorias,
                        elegida = categoria,
                        onElegir = { vm.filtrar(it) }
                    )

                    // Chips para elegir el radio de cercanía
                    SelectorRadio(
                        radios = vm.radios,
                        elegido = radio,
                        onElegir = { vm.elegirRadio(it) }
                    )

                    // Tarjeta con el lugar más cercano y el botón para ir hasta él
                    TarjetaMasCercano(
                        lugar = masCercano,
                        metros = metrosCercano,
                        onIr = { masCercano?.let { vm.seleccionar(it.id) } }
                    )
                }
            }

            // El mapa ocupa todo el espacio que queda libre (toda la pantalla en modo completo)
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                radio = radio,
                siguiendo = siguiendo,
                pantallaCompleta = pantallaCompleta,
                onPantallaCompleta = { pantallaCompleta = !pantallaCompleta },
                onSeleccionar = { vm.seleccionar(it) },
                ruta = ruta,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // La lista va debajo del mapa con altura fija
            AnimatedVisibility(visible = !pantallaCompleta, enter = entrar, exit = salir) {
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    dentroDelRadio = dentroDelRadio,
                    masCercanoId = masCercano?.id,
                    onClick = { vm.seleccionar(it) },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

// Botones para buscar o seguir la ubicación, y el estado actual debajo
@Composable
private fun PanelUbicacion(
    estado: UbicacionUi,
    siguiendo: Boolean,
    onBuscar: () -> Unit,
    onSeguir: () -> Unit,
    onAbrirAjustes: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val interaccionBuscar = remember { MutableInteractionSource() }
                Button(
                    onClick = onBuscar,
                    // Mientras sigue en vivo no hace falta pedir la ubicación a mano
                    enabled = estado !is UbicacionUi.Buscando && !siguiendo,
                    interactionSource = interaccionBuscar,
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    modifier = Modifier
                        .weight(1f)
                        .scale(escalaAlPresionar(interaccionBuscar))
                ) {
                    Icon(Icons.Filled.MyLocation, null, Modifier.size(ButtonDefaults.IconSize))
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Mi ubicación", maxLines = 1)
                }
                // Iniciar/Detener el seguimiento en vivo
                val interaccionSeguir = remember { MutableInteractionSource() }
                FilledTonalButton(
                    onClick = onSeguir,
                    interactionSource = interaccionSeguir,
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = if (siguiendo) {
                        ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    } else {
                        ButtonDefaults.filledTonalButtonColors()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .scale(escalaAlPresionar(interaccionSeguir))
                ) {
                    // El icono y el texto cambian con un fundido al iniciar o detener
                    AnimatedContent(targetState = siguiendo, label = "seguir") { activo ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (activo) Icons.Filled.Stop else Icons.Filled.Sensors,
                                null,
                                Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                            Text(if (activo) "Detener" else "Seguir en vivo", maxLines = 1)
                        }
                    }
                }
            }

            // Se anima solo al cambiar de tipo de estado, no con cada posición nueva
            AnimatedContent(
                targetState = estado,
                contentKey = { it::class },
                transitionSpec = {
                    (fadeIn(tween(250)) + slideInVertically(tween(250)) { it / 3 }) togetherWith
                        fadeOut(tween(150))
                },
                label = "estado"
            ) { e ->
                when (e) {
                    UbicacionUi.Inicial -> LineaEstado(
                        Icons.Filled.TouchApp,
                        "Pulsa un botón para ver dónde estás"
                    )
                    UbicacionUi.Buscando -> Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LineaEstado(Icons.Filled.GpsFixed, "Buscando señal GPS…")
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                    is UbicacionUi.Lista -> LineaPosicion(e, siguiendo)
                    is UbicacionUi.Error -> MensajeError(
                        error = e,
                        onAbrirAjustes = onAbrirAjustes,
                        onReintentar = onBuscar
                    )
                }
            }
        }
    }
}

@Composable
private fun LineaEstado(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(texto, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun LineaPosicion(e: UbicacionUi.Lista, siguiendo: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (siguiendo) {
            PuntoEnVivo(Color(0xFFE53935), tamano = 8.dp)
        } else {
            Icon(Icons.Filled.GpsFixed, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(8.dp))
        // Coordenadas con 5 decimales (más o menos 1 m de precisión)
        Text(
            text = String.format(
                Locale.US,
                "%.5f, %.5f",
                e.posicion.latitud,
                e.posicion.longitud
            ),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        if (siguiendo) {
            Spacer(Modifier.width(6.dp))
            Text("· en vivo", style = MaterialTheme.typography.bodySmall, color = Color(0xFFE53935))
        }
    }
}

@Composable
private fun TarjetaMasCercano(lugar: Lugar?, metros: Float?, onIr: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (lugar != null) {
                VerdeCercano.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (lugar != null) VerdeCercano else MaterialTheme.colorScheme.outline,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.NearMe, null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Más cercano",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // El nombre cambia deslizando hacia arriba cuando otro lugar pasa a ser el más cercano
                AnimatedContent(
                    targetState = if (lugar != null && metros != null) {
                        "${lugar.nombre} · ${formatearDistancia(metros)}"
                    } else {
                        "Busca tu ubicación primero"
                    },
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()) togetherWith
                            (slideOutVertically { -it } + fadeOut())
                    },
                    label = "cercano"
                ) { texto ->
                    Text(
                        texto,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (lugar != null) VerdeCercano else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // Lleva la cámara al lugar más cercano; deshabilitado sin ubicación
            FilledIconButton(
                onClick = onIr,
                enabled = lugar != null,
                colors = androidx.compose.material3.IconButtonDefaults.filledIconButtonColors(
                    containerColor = VerdeCercano,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Filled.Navigation, contentDescription = "Ir al más cercano")
            }
        }
    }
}
