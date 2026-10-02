package com.leon.tecsupstore.data

import com.leon.tecsupstore.model.Producto
import com.leon.tecsupstore.model.Seccion

val categorias = listOf("Todos", "Audio", "Wearables", "Accesorios", "Hogar")

val productos = listOf(
    Producto(1, "Audifonos", 89.00, "Audio"),
    Producto(2, "Smartwatch", 199.00, "Wearables"),
    Producto(3, "Funda celular", 25.00, "Accesorios"),
    Producto(4, "Parlante bluetooth", 120.00, "Audio"),
    Producto(5, "Pulsera fitness", 95.00, "Wearables"),
    Producto(6, "Cargador inalambrico", 60.00, "Accesorios"),
    Producto(7, "Lampara LED", 45.00, "Hogar"),
    Producto(8, "Organizador de cables", 18.00, "Hogar")
)

val secciones = listOf(
    Seccion("Mas vendidos", productos.filter { it.id in listOf(1, 2, 3) }),
    Seccion("Ofertas", productos.filter { it.id in listOf(4, 5, 6) }),
    Seccion("Para el hogar", productos.filter { it.id in listOf(7, 8) })
)
