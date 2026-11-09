package com.tecsup.fortnite.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

// Respuesta de la tienda: /v2/shop
@Serializable
data class TiendaResponse(val data: TiendaDto)

@Serializable
data class TiendaDto(val entries: List<EntradaDto> = emptyList())

// Cada entrada de la tienda puede ser un cosmético, una pista, un instrumento o un auto
@Serializable
data class EntradaDto(
    val finalPrice: Int = 0,
    val regularPrice: Int = 0,
    val bundle: LoteDto? = null,
    val layout: SeccionDto? = null,
    val colors: ColoresDto? = null,
    val outDate: String = "",
    val brItems: List<ItemDto>? = null,
    val tracks: List<JsonElement>? = null,
    val instruments: List<JsonElement>? = null,
    val cars: List<JsonElement>? = null
)

// Pack de varios cosméticos con un solo precio
@Serializable
data class LoteDto(val name: String = "", val image: String? = null)

// Sección de la tienda donde aparece la oferta (One Piece, Star Wars...)
@Serializable
data class SeccionDto(val name: String = "", val index: Int = 0)

// Colores oficiales de la tarjeta, en formato RRGGBBAA
@Serializable
data class ColoresDto(val color1: String = "", val color3: String = "")

// Respuesta del detalle: /v2/cosmetics/br/{id}
@Serializable
data class DetalleResponse(val data: ItemDto)

@Serializable
data class ItemDto(
    val id: String,
    val name: String = "",
    val description: String = "",
    val type: ValorDto? = null,
    val rarity: ValorDto? = null,
    val series: SerieDto? = null,
    val introduction: IntroduccionDto? = null,
    val images: ImagenesDto? = null
)

@Serializable
data class ValorDto(val value: String = "", val displayValue: String = "")

@Serializable
data class SerieDto(val value: String = "")

@Serializable
data class IntroduccionDto(val text: String = "")

@Serializable
data class ImagenesDto(
    val smallIcon: String? = null,
    val icon: String? = null,
    val featured: String? = null // render grande, sin fondo
)
