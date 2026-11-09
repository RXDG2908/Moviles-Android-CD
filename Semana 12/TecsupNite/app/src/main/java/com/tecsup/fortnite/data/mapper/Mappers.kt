package com.tecsup.fortnite.data.mapper

import com.tecsup.fortnite.data.local.ItemEntity
import com.tecsup.fortnite.data.remote.dto.EntradaDto
import com.tecsup.fortnite.data.remote.dto.ItemDto
import com.tecsup.fortnite.domain.model.Item
import java.time.Instant
import java.time.temporal.ChronoUnit

// Entrada de la tienda -> Entity. Si es un lote, se muestra con su nombre y su imagen
fun EntradaDto.aEntity(): ItemEntity {
    val item = brItems!![0]
    val lote = bundle
    return ItemEntity(
        id = item.id,
        titulo = lote?.name ?: item.name,
        subtitulo = if (lote != null) "Lote" else item.type?.displayValue ?: "",
        // El render grande (featured) se ve mejor en la tarjeta; si no hay, el icono
        imagenUrl = lote?.image ?: item.images?.featured ?: item.images?.icon ?: item.images?.smallIcon,
        precio = finalPrice,
        precioRegular = regularPrice,
        rareza = item.rarity?.displayValue ?: "",
        rarezaClave = item.rarity?.value ?: "",
        tipoClave = if (lote != null) "lote" else item.type?.value ?: "",
        lote = lote?.name ?: "",
        seccion = layout?.name?.trim() ?: "",
        seccionOrden = layout?.index ?: 999,
        color1 = colors?.color1 ?: "",
        color3 = colors?.color3 ?: "",
        terminaHoy = terminaEnUnDia(outDate)
    )
}

// true si la oferta sale de la tienda en menos de un día
private fun terminaEnUnDia(fecha: String): Boolean = try {
    Instant.parse(fecha).isBefore(Instant.now().plus(1, ChronoUnit.DAYS))
} catch (e: Exception) {
    false
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
    lote = lote,
    seccion = seccion,
    seccionOrden = seccionOrden,
    color1 = color1,
    color3 = color3,
    terminaHoy = terminaHoy
)

// Dto del detalle -> modelo de la UI (precio, lote, sección y colores vienen de Room)
fun ItemDto.aModeloDetalle(base: Item?) = Item(
    id = id,
    titulo = name,
    subtitulo = type?.displayValue ?: "",
    imagenUrl = images?.featured ?: images?.icon ?: images?.smallIcon,
    esFavorito = base?.esFavorito ?: false,
    precio = base?.precio ?: 0,
    precioRegular = base?.precioRegular ?: 0,
    rareza = rarity?.displayValue ?: "",
    rarezaClave = rarity?.value ?: "",
    tipoClave = type?.value ?: "",
    lote = base?.lote ?: "",
    seccion = base?.seccion ?: "",
    color1 = base?.color1 ?: "",
    color3 = base?.color3 ?: "",
    terminaHoy = base?.terminaHoy ?: false,
    descripcion = description,
    serie = series?.value ?: "",
    introduccion = introduction?.text ?: ""
)
