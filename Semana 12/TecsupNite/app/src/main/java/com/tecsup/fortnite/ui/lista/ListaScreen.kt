package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro

// Desde este precio el cosmético va en la sección Destacados
private const val PRECIO_DESTACADO = 1500

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

    // Un chip por cada tipo que hay en la tienda
    val tipos = remember(datos) { datos.distinctBy { it.tipoClave } }
    val visibles = remember(datos, busqueda, tipo) {
        datos.filter {
            (tipo == "" || it.tipoClave == tipo) && it.titulo.contains(busqueda, ignoreCase = true)
        }.sortedByDescending { it.precio }
    }
    val destacados = visibles.filter { it.precio >= PRECIO_DESTACADO }
    val diarios = visibles.filter { it.precio < PRECIO_DESTACADO }

    // Más columnas si la pantalla está girada; el buscador y los chips se desplazan con la lista
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier,
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (aviso != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Oro)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.WifiOff, contentDescription = null, tint = Noche)
                    Text(
                        "  " + aviso,
                        style = MaterialTheme.typography.titleSmall,
                        color = Noche
                    )
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Busca un cosmético…", color = CelesteTexto) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Blanco) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Noche,
                    unfocusedContainerColor = Noche,
                    focusedTextColor = Blanco,
                    unfocusedTextColor = Blanco,
                    cursorColor = Oro,
                    focusedBorderColor = Oro,
                    unfocusedBorderColor = Borde
                )
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item { ChipTipo("Todos", datos.size, tipo == "") { tipo = "" } }
                items(tipos) { t ->
                    ChipTipo(t.subtitulo, datos.count { it.tipoClave == t.tipoClave }, tipo == t.tipoClave) { tipo = t.tipoClave }
                }
            }
        }
        if (visibles.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Vacio("Sin resultados", "Prueba con otro nombre o con otro tipo")
            }
        }
        seccion("Destacados", destacados, true, onItemClick, onFavorito)
        seccion("Diarios", diarios, false, onItemClick, onFavorito)
    }
}

// Título de sección y sus tarjetas; los destacados ocupan dos columnas
private fun LazyGridScope.seccion(
    titulo: String,
    lista: List<Item>,
    grande: Boolean,
    onItemClick: (String) -> Unit,
    onFavorito: (String) -> Unit
) {
    if (lista.isEmpty()) return
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column(Modifier.padding(top = 12.dp)) {
            Text(titulo.uppercase(), style = MaterialTheme.typography.displaySmall, color = Blanco)
            Text(
                "${lista.size} objetos",
                style = MaterialTheme.typography.titleSmall,
                color = CelesteTexto
            )
        }
    }
    items(
        lista,
        key = { it.id },
        span = { GridItemSpan(if (grande) minOf(2, maxLineSpan) else 1) }
    ) { item ->
        ItemCard(
            item = item,
            onClick = { onItemClick(item.id) },
            onFavorito = { onFavorito(item.id) },
            destacado = grande
        )
    }
}

// Chip en forma de píldora con su contador, como en la web
@Composable
private fun ChipTipo(texto: String, cantidad: Int, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(texto, style = MaterialTheme.typography.titleSmall)
                Text(
                    "$cantidad",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (seleccionado) Noche else NocheClara)
                        .padding(horizontal = 7.dp, vertical = 1.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (seleccionado) Oro else Blanco
                )
            }
        },
        shape = RoundedCornerShape(50),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = Borde,
            selectedBorderColor = Oro
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Noche,
            labelColor = Blanco,
            selectedContainerColor = Oro,
            selectedLabelColor = Noche
        )
    )
}