package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.tecsup.fortnite.ui.theme.CieloFuerte
import com.tecsup.fortnite.ui.theme.FormaInclinada
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro

// Desde este precio el cosmético se muestra en grande, al inicio de la tienda
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
    // Lo más caro primero, como la sección destacada de la tienda
    val visibles = remember(datos, busqueda, tipo) {
        datos.filter {
            (tipo == "" || it.tipoClave == tipo) && it.titulo.contains(busqueda, ignoreCase = true)
        }.sortedByDescending { it.precio }
    }

    // Más columnas si la pantalla está girada; el buscador y los chips se desplazan con la lista
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier = modifier,
        contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (aviso != null) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    aviso.uppercase(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(FormaInclinada())
                        .background(Oro)
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    color = Noche
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar en la tienda", color = CieloFuerte) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Noche) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = FormaInclinada(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Blanco,
                    unfocusedContainerColor = Blanco,
                    focusedTextColor = Noche,
                    unfocusedTextColor = Noche,
                    cursorColor = Noche,
                    focusedBorderColor = Oro,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                item { ChipTipo("Todos", tipo == "") { tipo = "" } }
                items(tipos) { t ->
                    ChipTipo(t.subtitulo, tipo == t.tipoClave) { tipo = t.tipoClave }
                }
            }
        }
        if (visibles.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Vacio("Sin resultados", "Prueba con otro nombre o con otro tipo")
            }
        }
        items(
            visibles,
            key = { it.id },
            span = { item -> GridItemSpan(if (item.precio >= PRECIO_DESTACADO) minOf(2, maxLineSpan) else 1) }
        ) { item ->
            ItemCard(
                item = item,
                onClick = { onItemClick(item.id) },
                onFavorito = { onFavorito(item.id) },
                destacado = item.precio >= PRECIO_DESTACADO
            )
        }
    }
}

@Composable
private fun ChipTipo(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = { Text(texto.uppercase(), style = MaterialTheme.typography.titleSmall) },
        shape = FormaInclinada(8.dp),
        border = null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Noche,
            labelColor = Blanco,
            selectedContainerColor = Oro,
            selectedLabelColor = Noche
        )
    )
}
