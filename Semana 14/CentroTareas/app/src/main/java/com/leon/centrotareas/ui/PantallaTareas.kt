// Pantalla principal: cabecera y las cuatro tarjetas de tareas, con iconos y animaciones.
package com.leon.centrotareas.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.centrotareas.model.CargaUi
import com.leon.centrotareas.model.PrimosUi
import com.leon.centrotareas.model.RegistroTiempo
import com.leon.centrotareas.viewmodel.TareasViewModel

@Composable
fun PantallaTareas(vm: TareasViewModel) {
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val progreso by vm.progreso.collectAsStateWithLifecycle()
    val corriendo by vm.corriendo.collectAsStateWithLifecycle()
    val primos by vm.primos.collectAsStateWithLifecycle()
    val carga by vm.carga.collectAsStateWithLifecycle()
    val simularError by vm.simularError.collectAsStateWithLifecycle()
    val tiempos by vm.tiempos.collectAsStateWithLifecycle()

    // Hay trabajo en segundo plano si cualquiera de las tareas está corriendo
    val ocupado = corriendo || primos is PrimosUi.Calculando || carga == CargaUi.Cargando

    Scaffold(topBar = { Cabecera(ocupado) }) { relleno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(relleno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TarjetaProgreso(
                mensaje = mensaje,
                progreso = progreso,
                corriendo = corriendo,
                alIniciar = { vm.tareaLarga() },
                alCancelar = { vm.cancelar() }
            )
            TarjetaPrimos(
                primos = primos,
                alContar = { vm.contar(it) },
                alCancelar = { vm.cancelarPrimos() }
            )
            TarjetaCargas(
                carga = carga,
                simularError = simularError,
                alCambiarError = { vm.cambiarSimularError(it) },
                alUnaPorUna = { vm.cargarUnaPorUna() },
                alALaVez = { vm.cargarALaVez() }
            )
            TarjetaTiempos(tiempos = tiempos, alLimpiar = { vm.limpiarTiempos() })
        }
    }
}

// Cabecera con degradado azul y un aviso de que el hilo principal sigue libre
@Composable
fun Cabecera(ocupado: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(Color(0xFF0D47A1), Color(0xFF1976D2)))
            )
            // Deja libre la barra de estado (Android 15 dibuja de borde a borde)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Speed, contentDescription = null, tint = Color.White)
            Text(
                "CentroTareas",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Text(
            "Hilos y corrutinas sin congelar la pantalla",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f)
        )
        Spacer(Modifier.height(10.dp))
        Surface(color = Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(50)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                PuntoLatiendo(if (ocupado) Color(0xFFFFD54F) else Color(0xFF69F0AE))
                AnimatedContent(targetState = ocupado, label = "estadoHilo") { trabajando ->
                    Text(
                        if (trabajando) "Trabajando en segundo plano · la UI responde"
                        else "Hilo principal libre",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

// ---------- Tarjeta 1: tarea con progreso ----------
@Composable
fun TarjetaProgreso(
    mensaje: String,
    progreso: Float,
    corriendo: Boolean,
    alIniciar: () -> Unit,
    alCancelar: () -> Unit
) {
    var toques by rememberSaveable { mutableIntStateOf(0) }
    // Rebote del botón en cada toque
    val rebote = remember { Animatable(1f) }
    LaunchedEffect(toques) {
        if (toques > 0) {
            rebote.animateTo(1.08f, tween(70))
            rebote.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
    }
    // La barra se mueve suave entre un valor y el siguiente
    val avance by animateFloatAsState(progreso, tween(150), label = "avance")
    val colorBarra by animateColorAsState(
        when (mensaje) {
            "Terminó" -> VerdeExito
            "Cancelado" -> MaterialTheme.colorScheme.outline
            else -> MaterialTheme.colorScheme.primary
        },
        label = "colorBarra"
    )

    TarjetaTarea("Tarea con progreso", Icons.Filled.Sync, MaterialTheme.colorScheme.primary, 0) {
        // Prueba de vida: si deja de sumar, la pantalla está congelada
        Button(
            onClick = { toques++ },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = rebote.value
                    scaleY = rebote.value
                }
        ) {
            Icon(Icons.Filled.TouchApp, contentDescription = null)
            Text("Tócame: ", modifier = Modifier.padding(start = 8.dp))
            // El número entra desde abajo cada vez que cambia
            AnimatedContent(
                targetState = toques,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith
                        (slideOutVertically { -it } + fadeOut())
                },
                label = "toques"
            ) { valor ->
                Text("$valor", fontWeight = FontWeight.Bold)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = alIniciar, enabled = !corriendo, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null)
                Text("Iniciar", modifier = Modifier.padding(start = 6.dp))
            }
            OutlinedButton(onClick = alCancelar, enabled = corriendo, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Stop, contentDescription = null)
                Text("Cancelar", modifier = Modifier.padding(start = 6.dp))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { avance },
                color = colorBarra,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
            )
            Text(
                "${(progreso * 100).toInt()} %",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorBarra,
                modifier = Modifier
                    .width(64.dp)
                    .padding(start = 12.dp)
            )
        }
        // El mensaje cambia con un fundido y su icono propio
        AnimatedContent(targetState = mensaje, label = "mensaje") { texto ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                when (texto) {
                    "Trabajando..." -> IconoGirando(
                        Icons.Filled.HourglassTop,
                        MaterialTheme.colorScheme.primary,
                        Modifier.size(20.dp)
                    )
                    "Terminó" -> Icon(
                        Icons.Filled.CheckCircle, null, tint = VerdeExito, modifier = Modifier.size(20.dp)
                    )
                    "Cancelado" -> Icon(
                        Icons.Filled.Cancel, null,
                        tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp)
                    )
                    else -> Icon(
                        Icons.Filled.Info, null,
                        tint = MaterialTheme.colorScheme.outline, modifier = Modifier.size(20.dp)
                    )
                }
                Text(texto, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

// ---------- Tarjeta 2: contar números primos ----------
@Composable
fun TarjetaPrimos(primos: PrimosUi, alContar: (Int) -> Unit, alCancelar: () -> Unit) {
    var textoN by rememberSaveable { mutableStateOf("5000000") }
    val calculando = primos is PrimosUi.Calculando
    val morado = Color(0xFF6A1B9A)

    TarjetaTarea("Contar números primos", Icons.Filled.Memory, morado, 1) {
        OutlinedTextField(
            value = textoN,
            onValueChange = { textoN = it },
            label = { Text("Contar primos hasta") },
            leadingIcon = { Icon(Icons.Filled.Tag, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = !calculando,
            modifier = Modifier.fillMaxWidth()
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    // Ignora textos que no son número o son menores que 2
                    val n = textoN.toIntOrNull()
                    if (n != null && n >= 2) alContar(n)
                },
                enabled = !calculando,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Filled.Calculate, contentDescription = null)
                Text("Contar", modifier = Modifier.padding(start = 6.dp))
            }
            OutlinedButton(onClick = alCancelar, enabled = calculando, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Stop, contentDescription = null)
                Text("Cancelar", modifier = Modifier.padding(start = 6.dp))
            }
        }
        AnimatedContent(
            targetState = primos,
            // Solo anima al cambiar de tipo de estado, no en cada porcentaje
            contentKey = { it::class },
            transitionSpec = { (fadeIn() + scaleIn(initialScale = 0.92f)) togetherWith fadeOut() },
            label = "primos"
        ) { estado ->
            when (estado) {
                PrimosUi.Inicial -> Text(
                    "Elige un número y pulsa Contar.",
                    color = MaterialTheme.colorScheme.outline
                )
                is PrimosUi.Calculando -> {
                    val avance by animateFloatAsState(estado.porcentaje, tween(120), label = "avancePrimos")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        LinearProgressIndicator(
                            progress = { avance },
                            color = morado,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier.fillMaxWidth().height(10.dp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconoGirando(Icons.Filled.HourglassTop, morado, Modifier.size(20.dp))
                            Text(
                                "Calculando... ${(estado.porcentaje * 100).toInt()} %",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
                is PrimosUi.Listo -> {
                    // El número sube desde cero hasta el resultado
                    val contado by animateIntAsState(estado.cantidad, tween(900), label = "contado")
                    Surface(color = FondoExito, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            FilaIcono(Icons.Filled.CheckCircle, "$contado primos", VerdeExito, negrita = true)
                            FilaIcono(Icons.Filled.Timer, "en ${estado.milisegundos} ms", VerdeExito)
                        }
                    }
                }
                PrimosUi.Cancelado -> Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(14.dp)) {
                        FilaIcono(Icons.Filled.Block, "Cancelado", MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

// ---------- Tarjeta 3: dos cargas ----------
@Composable
fun TarjetaCargas(
    carga: CargaUi,
    simularError: Boolean,
    alCambiarError: (Boolean) -> Unit,
    alUnaPorUna: () -> Unit,
    alALaVez: () -> Unit
) {
    val cargando = carga == CargaUi.Cargando
    val naranja = Color(0xFFE65100)

    TarjetaTarea("Dos cargas", Icons.Filled.CloudDownload, naranja, 2) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = alUnaPorUna, enabled = !cargando, modifier = Modifier.weight(1f)) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Text("Una por una", modifier = Modifier.padding(start = 6.dp))
            }
            Button(onClick = alALaVez, enabled = !cargando, modifier = Modifier.weight(1f)) {
                Icon(Icons.Filled.Bolt, contentDescription = null)
                Text("A la vez", modifier = Modifier.padding(start = 6.dp))
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Switch(
                checked = simularError,
                onCheckedChange = alCambiarError,
                // Icono dentro del interruptor: sin red cuando está activo
                thumbContent = {
                    Icon(
                        if (simularError) Icons.Filled.WifiOff else Icons.Filled.Wifi,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.error,
                    checkedThumbColor = MaterialTheme.colorScheme.onError
                )
            )
            Text("Simular error", modifier = Modifier.padding(start = 10.dp))
        }
        AnimatedVisibility(
            visible = carga != CargaUi.Inicial,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            AnimatedContent(
                targetState = carga,
                transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(150)) },
                label = "carga"
            ) { estado ->
                when (estado) {
                    CargaUi.Inicial -> Spacer(Modifier.height(1.dp))
                    CargaUi.Cargando -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(color = naranja, modifier = Modifier.size(28.dp))
                        Text("Cargando perfil y pedidos...", modifier = Modifier.padding(start = 12.dp))
                    }
                    is CargaUi.Exito -> Surface(
                        color = FondoExito,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            FilaIcono(
                                if (estado.modo == "A la vez") Icons.Filled.Bolt else Icons.AutoMirrored.Filled.List,
                                "Modo: ${estado.modo}", VerdeExito, negrita = true
                            )
                            FilaIcono(Icons.Filled.Person, "Perfil: ${estado.perfil}", VerdeExito)
                            FilaIcono(Icons.Filled.ShoppingCart, "Pedidos: ${estado.pedidos}", VerdeExito)
                            FilaIcono(Icons.Filled.Timer, "Tiempo: ${estado.milisegundos} ms", VerdeExito)
                        }
                    }
                    is CargaUi.Error -> Sacudida(clave = estado) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilaIcono(
                                    Icons.Filled.CloudOff,
                                    estado.mensaje,
                                    MaterialTheme.colorScheme.onErrorContainer,
                                    negrita = true
                                )
                                // Vuelve a intentar la carga A la vez
                                Button(
                                    onClick = alALaVez,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(Icons.Filled.Refresh, contentDescription = null)
                                    Text("Reintentar", modifier = Modifier.padding(start = 6.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------- Tarjeta 4: comparación de tiempos ----------
@Composable
fun TarjetaTiempos(tiempos: List<RegistroTiempo>, alLimpiar: () -> Unit) {
    TarjetaTarea("Comparación de tiempos", Icons.Filled.BarChart, Color(0xFF00897B), 3) {
        if (tiempos.isEmpty()) {
            Text(
                "Aún no hay tiempos. Usa Una por una y A la vez.",
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            // La barra más larga es el tiempo mayor; las demás se miden contra ella
            val maximo = tiempos.maxOf { it.milisegundos }.coerceAtLeast(1L)
            tiempos.forEachIndexed { i, registro ->
                BarraTiempo("${i + 1}. ${registro.modo}", registro, maximo)
            }
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (modo in listOf("Una por una", "A la vez")) {
                    val delModo = tiempos.filter { it.modo == modo }
                    if (delModo.isNotEmpty()) {
                        val promedio = delModo.map { it.milisegundos }.average().toLong()
                        Surface(
                            color = colorDeModo(modo).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    "Promedio $modo (${delModo.size})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = colorDeModo(modo)
                                )
                                Text(
                                    "$promedio ms",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colorDeModo(modo)
                                )
                            }
                        }
                    }
                }
            }
        }
        TextButton(onClick = alLimpiar, enabled = tiempos.isNotEmpty()) {
            Icon(Icons.Filled.DeleteSweep, contentDescription = null)
            Text("Limpiar", modifier = Modifier.padding(start = 6.dp))
        }
    }
}

// Azul para una por una y verde para a la vez
@Composable
fun colorDeModo(modo: String): Color =
    if (modo == "A la vez") VerdeExito else MaterialTheme.colorScheme.primary

// Una fila de la tabla: nombre, barra que crece hasta su largo y el tiempo
@Composable
fun BarraTiempo(etiqueta: String, registro: RegistroTiempo, maximo: Long) {
    // Empieza en cero y crece al aparecer
    val objetivo = registro.milisegundos.toFloat() / maximo
    val largo = remember(registro) { Animatable(0f) }
    LaunchedEffect(registro, maximo) { largo.animateTo(objetivo, tween(700)) }
    val color = colorDeModo(registro.modo)

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(etiqueta, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
            Text("${registro.milisegundos} ms", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(largo.value)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}
