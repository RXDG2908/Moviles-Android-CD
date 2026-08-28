package com.leon.lab02carritokotlin

// =====================================================================
// Laboratorio 02 - Carrito de compras en Kotlin
// Rama R2 (con IA) - Bloque 1: POO base
//
// El carrito deja de ser una lista suelta que se pasa de funcion en
// funcion, y pasa a ser un objeto con su propio estado y operaciones.
// El reporte tambien se separa: una clase calcula y otra muestra.
// =====================================================================

const val TASA_IGV = 0.18

class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    val importe: Double
        get() = precio * cantidad
}

class Carrito(val cliente: String) {

    private val productos = mutableListOf<Producto>()

    val cantidadProductos: Int
        get() = productos.size

    fun agregar(producto: Producto) {
        productos.add(producto)
    }

    fun buscar(nombre: String): Producto? {
        return productos.find { it.nombre == nombre }
    }

    fun eliminar(nombre: String) {
        productos.removeIf { it.nombre == nombre }
    }

    fun calcularSubtotal(): Double {
        var subtotal = 0.0
        for (p in productos) {
            subtotal += p.importe
        }
        return subtotal
    }

    fun calcularIGV(): Double = calcularSubtotal() * TASA_IGV

    fun calcularTotal(): Double = calcularSubtotal() + calcularIGV()

    fun calcularDescuento(): Double {
        val total = calcularTotal()
        return when {
            total > 5000 -> total * 0.10
            total > 3000 -> total * 0.05
            else -> 0.0
        }
    }

    fun productoMasCaro(): Producto? = productos.maxByOrNull { it.precio }

    fun listar(): List<Producto> = productos
}

class ReporteConsola(private val carrito: Carrito) {

    fun imprimirCabecera() {
        println("=========================================")
        println("   CARRITO DE COMPRAS - TIENDA TECSUP")
        println("=========================================")
        println("Cliente: ${carrito.cliente}")
        println()
    }

    fun imprimirProductosAgregados() {
        for (producto in carrito.listar()) {
            println("Producto agregado: ${producto.nombre}")
        }
        println()
    }

    fun imprimirDetalle() {
        println("--------- DETALLE DEL CARRITO ---------")
        var i = 1
        for (p in carrito.listar()) {
            println(String.format("%d. %-20s x%d  S/ %8.2f", i, p.nombre, p.cantidad, p.importe))
            i++
        }
        println("---------------------------------------")
    }

    fun imprimirTotales() {
        println("Cantidad de productos : ${carrito.cantidadProductos}")
        println(String.format("%-22s: S/ %8.2f", "Subtotal", carrito.calcularSubtotal()))
        println(String.format("%-22s: S/ %8.2f", "IGV (18%)", carrito.calcularIGV()))
        println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", carrito.calcularTotal()))
    }

    fun imprimirProductoMasCaro() {
        val masCaro = carrito.productoMasCaro()
        if (masCaro != null) {
            println("Producto mas caro: ${masCaro.nombre} " +
                    String.format("(S/ %.2f)", masCaro.precio))
        }
    }

    fun imprimirDescuento() {
        val total = carrito.calcularTotal()
        val descuento = carrito.calcularDescuento()

        if (descuento > 0) {
            if (total > 5000) {
                println("Descuento aplicado: 10% por compra mayor a S/ 5000")
            } else {
                println("Descuento aplicado: 5% por compra mayor a S/ 3000")
            }
        } else {
            println("Descuento aplicado: 0%")
        }

        imprimirTotalConDescuento()
    }

    fun imprimirTotalConDescuento() {
        println(String.format("%-22s: S/ %8.2f", "TOTAL CON DESCUENTO",
            carrito.calcularTotal() - carrito.calcularDescuento()))
    }

    fun imprimirDespedida() {
        println()
        println("Gracias por su compra, ${carrito.cliente}!")
    }
}

fun main() {
    val carrito = Carrito("Renzo Leon")
    val reporte = ReporteConsola(carrito)

    carrito.agregar(Producto("Laptop HP", 2500.0, 1))
    carrito.agregar(Producto("Mouse Logitech", 45.5, 2))
    carrito.agregar(Producto("Audifonos Sony", 120.0, 1))
    carrito.agregar(Producto("USB Kingston 64GB", 25.0, 3))

    reporte.imprimirCabecera()
    reporte.imprimirProductosAgregados()
    reporte.imprimirDetalle()
    reporte.imprimirTotales()
    println("---------------------------------------")
    reporte.imprimirProductoMasCaro()
    reporte.imprimirDescuento()

    // ---------- Reto adicional ----------

    println()
    println("--------- BUSCAR PRODUCTO ---------")
    val encontrado = carrito.buscar("Mouse Logitech")
    if (encontrado != null) {
        println("Producto encontrado: ${encontrado.nombre}")
        println("Precio: S/ ${String.format("%.2f", encontrado.precio)}")
        println("Cantidad: ${encontrado.cantidad}")
    } else {
        println("Producto no encontrado")
    }

    println()
    println("--------- ELIMINAR PRODUCTO ---------")
    carrito.eliminar("USB Kingston 64GB")
    println("Producto eliminado: USB Kingston 64GB")
    println()

    reporte.imprimirDetalle()
    reporte.imprimirTotales()
    reporte.imprimirTotalConDescuento()

    reporte.imprimirDespedida()
}
