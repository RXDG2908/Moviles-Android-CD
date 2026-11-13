// Modelo de un cosmetico que usa la interfaz.
package com.tecsup.fortnite.domain.model

// Cosmético de la tienda tal como lo usa la pantalla
data class Item(
    val id: String,
    val titulo: String,
    val subtitulo: String,        // tipo en español: Traje, Pico, Emote...
    val imagenUrl: String?,
    val esFavorito: Boolean = false,
    val precio: Int = 0,          // V-Bucks
    val precioRegular: Int = 0,
    val rareza: String = "",      // texto en español
    val rarezaClave: String = "", // epic, rare, icon...
    val tipoClave: String = "",   // outfit, pickaxe, emote...
    val lote: String = "",        // nombre del lote si el precio es de un pack
    val seccion: String = "",     // sección de la tienda
    val seccionOrden: Int = 0,
    val color1: String = "",      // colores oficiales de la tarjeta (RRGGBBAA)
    val color3: String = "",
    val terminaHoy: Boolean = false,
    val descripcion: String = "",
    val serie: String = "",
    val introduccion: String = ""
)
