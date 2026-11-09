package com.tecsup.fortnite.data.mapper

import com.tecsup.fortnite.data.local.ItemEntity
import com.tecsup.fortnite.data.remote.dto.ItemDto
import com.tecsup.fortnite.domain.model.Item

// Dto -> Entity (la lista de la tienda trae el precio de la entrada)
fun ItemDto.aEntity(precio: Int, precioRegular: Int, esFavorito: Boolean) = ItemEntity(
    id = id,
    titulo = name,
    subtitulo = type?.displayValue ?: "",
    imagenUrl = images?.icon ?: images?.smallIcon,
    precio = precio,
    precioRegular = precioRegular,
    rareza = rarity?.displayValue ?: "",
    rarezaClave = rarity?.value ?: "",
    tipoClave = type?.value ?: "",
    esFavorito = esFavorito
)

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
    tipoClave = tipoClave
)

// Dto del detalle -> modelo de la UI (el precio y el favorito vienen de Room)
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
    descripcion = description,
    serie = series?.value ?: "",
    introduccion = introduction?.text ?: ""
)
