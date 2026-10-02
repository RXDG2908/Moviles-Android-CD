package com.leon.tecsupstore.data

import java.util.Locale

// Un producto de la tienda
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val descripcion: String
) {
    // Precio con punto decimal en cualquier idioma del celular: "S/ 89.00"
    val precioTexto: String
        get() = String.format(Locale.US, "S/ %.2f", precio)
}

// Una sección de la tienda con su título y sus productos
data class Seccion(
    val titulo: String,
    val productos: List<Producto>
)

// Un pedido ya hecho por el usuario
data class Pedido(
    val codigo: String,
    val producto: String,
    val fecha: String,
    val estado: String            // "En camino" o "Entregado"
)

// Usuario con la sesión iniciada (sale en el Perfil y en el menú)
data class Usuario(
    val nombre: String,
    val correo: String
) {
    // "Maria Rojas" -> "MR": primera letra de cada palabra
    val iniciales: String
        get() = nombre.split(" ").take(2).joinToString("") { it.take(1) }
}

val usuario = Usuario("Maria Rojas", "maria@tecsup.edu.pe")

val productos = listOf(
    Producto(1, "Audifonos", 89.00, "Audifonos inalambricos con cancelacion de ruido y 20 horas de bateria."),
    Producto(2, "Smartwatch", 199.00, "Reloj inteligente con monitor de ritmo cardiaco y notificaciones."),
    Producto(3, "Funda celular", 25.00, "Funda de silicona resistente a golpes."),
    Producto(4, "Parlante bluetooth", 120.00, "Parlante portatil resistente al agua."),
    Producto(5, "Pulsera fitness", 95.00, "Cuenta pasos, calorias y horas de sueno."),
    Producto(6, "Cargador inalambrico", 60.00, "Base de carga rapida para celulares."),
    Producto(7, "Lampara LED", 45.00, "Lampara de escritorio con tres niveles de brillo."),
    Producto(8, "Organizador de cables", 18.00, "Mantiene ordenados los cables del escritorio.")
)

// Cada sección toma sus productos de la lista general por id
val secciones = listOf(
    Seccion("Mas vendidos", productos.filter { it.id in listOf(1, 2, 3) }),
    Seccion("Ofertas", productos.filter { it.id in listOf(4, 5, 6) }),
    Seccion("Para el hogar", productos.filter { it.id in listOf(7, 8) })
)

val pedidos = listOf(
    Pedido("#1024", "Smartwatch", "28/09", "En camino"),
    Pedido("#0987", "Funda celular", "15/09", "Entregado"),
    Pedido("#0950", "Audifonos", "02/09", "Entregado")
)
