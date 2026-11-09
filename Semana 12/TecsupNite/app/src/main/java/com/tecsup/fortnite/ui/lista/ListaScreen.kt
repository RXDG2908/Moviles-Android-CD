package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.Vacio
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.Borde
import com.tecsup.fortnite.ui.theme.CelesteTexto
import com.tecsup.fortnite.ui.theme.NocheClara
import com.tecsup.fortnite.ui.theme.Oro
import com.tecsup.fortnite.ui.theme.SombraPanel
import com.tecsup.fortnite.ui.theme.TintaAm
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime

private const val ORDEN_TIENDA = "Como en Fortnite"
private const val ORDEN_MENOR = "Menor precio"
private const val ORDEN_MAYOR = "Mayor precio"

@Composable
fun ListaScreen(
    estado: UiState<List<Item>>,
    aviso: String?,
    onReintentar: () -> Unit,
    onItemClick: (String) -> Unit,
    onFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> ErrorView(estado.mensaje, onReintentar, modifier)
        is UiState.Exito -> {
            if (estado.datos.isEmpty()) {
                Vacio("Tienda vacía", "Sincroniza para traer los cosméticos de hoy", modifier)
            } else {
                ContenidoLista(estado.datos, aviso, onItemClick, onFavorito, modifier)
            }
        }
    }
}

@Composable
private fun ContenidoLista(
    datos: List<Item>,
    aviso: String?,
    onItemClick: (String) -> Unit,
    onFavorito: (String) -> Unit,
    modifier: Modifier
) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("") }
    var conDescuento by rememberSaveable { mutableStateOf(false) }
    var terminaHoy by rememberSaveable { mutableStateOf(false) }
    var orden by rememberSaveable { mutableStateOf(ORDEN_TIENDA) }

    // Un chip por cada tipo que hay en la tienda, con su cantidad
    val tipos = remember(datos) { datos.distinctBy { it.tipoClave } }
    val visibles = remember(datos, busqueda, tipo, conDescuento, terminaHoy) {
        datos.filter {
            (tipo == "" || it.tipoClave == tipo) &&
                (!conDescuento || it.precioRegular > it.precio) &&
                (!terminaHoy || it.terminaHoy) &&
                (it.titulo.contains(busqueda, ignoreCase = true) || it.seccion.contains(busqueda, ignoreCase = true))
        }
    }
    // Secciones en el orden de la tienda; las que no tienen nombre van al final
    val secciones = remember(visibles) {
        visibles.groupBy { it.seccion.ifBlank { "Más de la tienda" } }
            .toList()
            .sortedBy { (nombre, lista) -> if (nombre == "Más de la tienda") 9999 else lista.minOf { it.seccionOrden } }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cabecera de la página, como .pag-cab
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Text("TIENDA DE HOY", style = MaterialTheme.typography.displayMedium, color = Blanco)
                Text(
                    "Todo lo que hay hoy en la tienda de Fortnite. Toca un objeto para ver su detalle.",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Blanco
                )
                Reloj(Modifier.padding(top = 12.dp))
            }
        }
        if (aviso != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Oro)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.WifiOff, contentDescription = null, tint = TintaAm)
                    Text("  $aviso", style = MaterialTheme.typography.titleSmall, color = TintaAm)
                }
            }
        }
        // Barra: búsqueda y orden
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Busca skin, lote o sección…", color = CelesteTexto) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Blanco) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SombraPanel,
                        unfocusedContainerColor = SombraPanel,
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco,
                        cursorColor = Oro,
                        focusedBorderColor = Oro,
                        unfocusedBorderColor = Borde.copy(alpha = 0.5f)
                    )
                )
                SelectorOrden(orden) { orden = it }
            }
        }
        // Filtros: tipos y otros, con conteos
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Chip("Todos", datos.size, tipo == "") { tipo = "" } }
                    items(tipos) { t ->
                        Chip(t.subtitulo, datos.count { it.tipoClave == t.tipoClave }, tipo == t.tipoClave) {
                            tipo = if (tipo == t.tipoClave) "" else t.tipoClave
                        }
                    }
                }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        Chip("Con descuento", datos.count { it.precioRegular > it.precio }, conDescuento, Icons.Filled.Sell) {
                            conDescuento = !conDescuento
                        }
                    }
                    item {
                        Chip("Termina hoy", datos.count { it.terminaHoy }, terminaHoy, Icons.Filled.Schedule) {
                            terminaHoy = !terminaHoy
                        }
                    }
                }
            }
        }
        // Resumen, como .resumen
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                if (orden == ORDEN_TIENDA) "${visibles.size} objetos en ${secciones.size} secciones"
                else "${visibles.size} objetos",
                style = MaterialTheme.typography.titleSmall,
                color = CelesteTexto
            )
        }
        if (visibles.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Vacio("Sin resultados", "Prueba con otro nombre o quita los filtros")
            }
        }
        if (orden == ORDEN_TIENDA) {
            for ((nombre, lista) in secciones) {
                seccion(nombre, lista, onItemClick, onFavorito)
            }
        } else {
            val ordenados = if (orden == ORDEN_MENOR) visibles.sortedBy { it.precio } else visibles.sortedByDescending { it.precio }
            items(ordenados, key = { it.id }) { item ->
                ItemCard(item, { onItemClick(item.id) }, { onFavorito(item.id) })
            }
        }
    }
}

// Título de la sección (Burbank) con su cantidad, y sus tarjetas
private fun LazyGridScope.seccion(
    titulo: String,
    lista: List<Item>,
    onItemClick: (String) -> Unit,
    onFavorito: (String) -> Unit
) {
    item(key = "sec-$titulo", span = { GridItemSpan(maxLineSpan) }) {
        Column(Modifier.padding(top = 14.dp)) {
            Text(titulo.uppercase(), style = MaterialTheme.typography.headlineLarge, color = Blanco)
            Text("${lista.size} objetos", style = MaterialTheme.typography.titleSmall, color = CelesteTexto)
        }
    }
    items(lista, key = { it.id }) { item ->
        ItemCard(item, { onItemClick(item.id) }, { onFavorito(item.id) })
    }
}

// Cuenta regresiva hasta el cambio de tienda (00:00 UTC), como .reloj
@Composable
private fun Reloj(modifier: Modifier = Modifier) {
    var texto by remember { mutableStateOf("--:--:--") }
    LaunchedEffect(Unit) {
        while (true) {
            val ahora = ZonedDateTime.now(ZoneOffset.UTC)
            val cambio = LocalDate.now(ZoneOffset.UTC).plusDays(1).atStartOfDay(ZoneOffset.UTC)
            val s = Duration.between(ahora, cambio).seconds
            texto = "%02d:%02d:%02d".format(s / 3600, (s % 3600) / 60, s % 60)
            delay(1000)
        }
    }
    Row(
        modifier
            .clip(RoundedCornerShape(50))
            .background(SombraPanel)
            .border(1.dp, Borde.copy(alpha = 0.5f), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Schedule, contentDescription = null, tint = Blanco)
        Text("  Cambia en ", style = MaterialTheme.typography.titleSmall, color = Blanco)
        Text(texto, style = MaterialTheme.typography.titleSmall, color = Oro)
    }
}

// Selector de orden, como el <select> de la web
@Composable
private fun SelectorOrden(actual: String, onCambio: (String) -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    Box {
        Row(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(SombraPanel)
                .border(1.dp, Borde.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .clickable(onClickLabel = "Cambiar orden") { abierto = true }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(actual, style = MaterialTheme.typography.titleSmall, color = Blanco)
            Spacer(Modifier.width(6.dp))
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Oro)
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            for (opcion in listOf(ORDEN_TIENDA, ORDEN_MENOR, ORDEN_MAYOR)) {
                DropdownMenuItem(
                    text = { Text(opcion, style = MaterialTheme.typography.bodyLarge) },
                    onClick = { onCambio(opcion); abierto = false }
                )
            }
        }
    }
}

// Chip de filtro como .fchip: píldora con borde celeste y contador
@Composable
private fun Chip(
    texto: String,
    cantidad: Int,
    activo: Boolean,
    icono: ImageVector? = null,
    onClick: () -> Unit
) {
    val forma = RoundedCornerShape(50)
    Row(
        Modifier
            .height(40.dp)
            .clip(forma)
            .background(if (activo) Oro else SombraPanel)
            .border(1.dp, if (activo) Oro else Borde.copy(alpha = 0.45f), forma)
            .clickable(onClickLabel = "Filtrar por $texto") { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            Icon(icono, contentDescription = null, tint = if (activo) TintaAm else Blanco, modifier = Modifier.padding(end = 6.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelLarge, color = if (activo) TintaAm else Blanco)
        Text(
            "$cantidad",
            modifier = Modifier
                .padding(start = 7.dp)
                .clip(forma)
                .background(if (activo) TintaAm else NocheClara)
                .padding(horizontal = 7.dp, vertical = 1.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (activo) Oro else Blanco
        )
    }
}
