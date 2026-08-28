data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d  S/ %8.2f", i, p.nombre, p.cantidad, importe))
        i++
    }
    println("---------------------------------------")
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

// RETO ADICIONAL: buscar producto
fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre == nombre }
}

fun main() {
    println("=========================================")
    println("   CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    val nombreCliente = "Renzo Leon"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 25.0, 3))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }
    println()

    mostrarDetalle(carrito)

    println("Cantidad de productos : ${carrito.size}")

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-22s: S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", igv))
    println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", total))

    println("---------------------------------------")

    val masCaro = carrito.maxByOrNull { it.precio }

    if (masCaro != null) {
        println("Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio))
    }

    val descuento = calcularDescuento(total)

    if (descuento > 0) {
        if (total > 5000) {
            println("Descuento aplicado: 10% por compra mayor a S/ 5000")
        } else {
            println("Descuento aplicado: 5% por compra mayor a S/ 3000")
        }
    } else {
        println("Descuento aplicado: 0%")
    }

    println(String.format("%-22s: S/ %8.2f", "TOTAL CON DESCUENTO", total - descuento))

    // =========================================
    // RETO ADICIONAL
    // =========================================

    println()
    println("--------- BUSCAR PRODUCTO ---------")

    val productoBuscado = buscarProducto(carrito, "Mouse Logitech")

    if (productoBuscado != null) {
        println("Producto encontrado: ${productoBuscado.nombre}")
        println("Precio: S/ ${String.format("%.2f", productoBuscado.precio)}")
        println("Cantidad: ${productoBuscado.cantidad}")
    } else {
        println("Producto no encontrado")
    }

    // Eliminar producto usando removeIf
    println()
    println("--------- ELIMINAR PRODUCTO ---------")

    carrito.removeIf { it.nombre == "USB Kingston 64GB" }

    println("Producto eliminado: USB Kingston 64GB")
    println()

    // Mostrar nuevamente detalle y totales actualizados
    mostrarDetalle(carrito)

    println("Cantidad de productos : ${carrito.size}")

    val nuevoSubtotal = calcularSubtotal(carrito)
    val nuevoIgv = calcularIGV(nuevoSubtotal)
    val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIgv)

    println(String.format("%-22s: S/ %8.2f", "Subtotal", nuevoSubtotal))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", nuevoIgv))
    println(String.format("%-22s: S/ %8.2f", "TOTAL A PAGAR", nuevoTotal))

    val nuevoDescuento = calcularDescuento(nuevoTotal)

    println(String.format(
        "%-22s: S/ %8.2f",
        "TOTAL CON DESCUENTO",
        nuevoTotal - nuevoDescuento
    ))

    println()
    println("Gracias por su compra, $nombreCliente!")
}
