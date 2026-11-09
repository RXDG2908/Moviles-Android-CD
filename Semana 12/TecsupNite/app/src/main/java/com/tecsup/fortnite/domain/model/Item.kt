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
    val descripcion: String = "",
    val serie: String = "",
    val introduccion: String = ""
)
