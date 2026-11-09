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
    val esFavorito: Boolean = false
)
