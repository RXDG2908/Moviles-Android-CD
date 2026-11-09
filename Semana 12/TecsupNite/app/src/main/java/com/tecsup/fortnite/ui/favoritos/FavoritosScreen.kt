package com.tecsup.fortnite.ui.favoritos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.components.ErrorView
import com.tecsup.fortnite.ui.components.Vacio
import com.tecsup.fortnite.ui.lista.ItemCard

@Composable
fun FavoritosScreen(
    estado: UiState<List<Item>>,
    onItemClick: (String) -> Unit,
    onFavorito: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (estado) {
        is UiState.Cargando -> Cargando(modifier)
        is UiState.Error -> ErrorView(estado.mensaje, {}, modifier)
        is UiState.Exito -> {
            if (estado.datos.isEmpty()) {
                Vacio("Aún no tienes favoritos. Toca la estrella de un cosmético para guardarlo.", modifier)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier,
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(estado.datos, key = { it.id }) { item ->
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
}
