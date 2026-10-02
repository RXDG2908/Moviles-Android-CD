package com.leon.tecsupstore.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String
)

data class Seccion(
    val titulo: String,
    val productos: List<Producto>
)
