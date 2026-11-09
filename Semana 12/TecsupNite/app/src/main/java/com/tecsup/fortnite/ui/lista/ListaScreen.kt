package com.tecsup.fortnite.ui.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.Vacio

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
                Vacio("La tienda no tiene cosméticos por ahora", modifier)
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
    val tipos = datos.distinctBy { it.tipoClave }
    val visibles = datos.filter {
        (tipo == "" || it.tipoClave == tipo) && it.titulo.contains(busqueda, ignoreCase = true)
    }

    Column(modifier) {
        if (aviso != null) {
            Text(
                aviso,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Buscar cosmético") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(28.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = tipo == "",
                    onClick = { tipo = "" },
                    label = { Text("Todos") }
                )
            }
            items(tipos) { t ->
                FilterChip(
                    selected = tipo == t.tipoClave,
                    onClick = { tipo = t.tipoClave },
                    label = { Text(t.subtitulo) }
                )
            }
        }
        if (visibles.isEmpty()) {
            Vacio("No hay resultados para tu búsqueda")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(visibles, key = { it.id }) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onItemClick(item.id) },
                        onFavorito = { onFavorito(item.id) }
                    )
                }
            }
        }
    }
}
