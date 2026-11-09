package com.tecsup.fortnite.data.mapper

import com.tecsup.fortnite.data.local.ItemEntity
import com.tecsup.fortnite.data.remote.dto.EntradaDto
import com.tecsup.fortnite.data.remote.dto.ItemDto
import com.tecsup.fortnite.domain.model.Item

// Entrada de la tienda -> Entity. Si es un lote, se muestra con su nombre y su imagen
fun EntradaDto.aEntity(): ItemEntity {
    val item = brItems!![0]
    val lote = bundle
    return ItemEntity(
        id = item.id,
        titulo = lote?.name ?: item.name,
        subtitulo = if (lote != null) "Lote" else item.type?.displayValue ?: "",
        imagenUrl = lote?.image ?: item.images?.icon ?: item.images?.smallIcon,
        precio = finalPrice,
        precioRegular = regularPrice,
        rareza = item.rarity?.displayValue ?: "",
        rarezaClave = item.rarity?.value ?: "",
        tipoClave = if (lote != null) "lote" else item.type?.value ?: "",
        lote = lote?.name ?: ""
    )
}

// Entity -> modelo de la UI
fun ItemEntity.aModelo() = Item(
    id = id,
    titulo = titulo,
    subtitulo = subtitulo,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito,
    precio = precio,
    precioRegular = precioRegular,
    rareza = rareza,
    rarezaClave = rarezaClave,
    tipoClave = tipoClave,
    lote = lote
)

// Dto del detalle -> modelo de la UI (el precio, el lote y el favorito vienen de Room)
fun ItemDto.aModeloDetalle(base: Item?) = Item(
    id = id,
    titulo = name,
    subtitulo = type?.displayValue ?: "",
    imagenUrl = images?.icon ?: images?.smallIcon,
    esFavorito = base?.esFavorito ?: false,
    precio = base?.precio ?: 0,
    precioRegular = base?.precioRegular ?: 0,
    rareza = rarity?.displayValue ?: "",
    rarezaClave = rarity?.value ?: "",
    tipoClave = type?.value ?: "",
    lote = base?.lote ?: "",
    descripcion = description,
    serie = series?.value ?: "",
    introduccion = introduction?.text ?: ""
)
