// Tabla de Room: un item guardado en el celular.
package com.tecsup.fortnite.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val subtitulo: String,
    val imagenUrl: String?,
    val precio: Int,
    val precioRegular: Int,
    val rareza: String,
    val rarezaClave: String,
    val tipoClave: String,
    val lote: String = "", // nombre del lote si el precio es de un pack
    val seccion: String = "",
    val seccionOrden: Int = 0,
    val color1: String = "",
    val color3: String = "",
    val terminaHoy: Boolean = false,
    val esFavorito: Boolean = false
)
