package com.leon.lab02carritokotlin

// =====================================================================
// Rama R2 (con IA) - Bloque 4: Polimorfismo
//
// El reporte recorre la lista tratando a todos como ProductoBase, sin
// preguntar de que tipo son. Cada subclase responde a etiqueta() y a
// descripcionGarantia() a su manera, y es Kotlin quien decide en tiempo
// de ejecucion cual version se ejecuta.
// =====================================================================

const val TASA_IGV = 0.18

abstract class ProductoBase(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
) {
    abstract val tipo: String

    abstract fun calcularImporte(): Double

    open fun etiqueta(): String = "$nombre ($tipo)"

    open fun descripcionGarantia(): String = "Sin garantia"
}

class ProductoElectronico(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val mesesGarantia: Int
) : ProductoBase(nombre, precio, cantidad) {

    override val tipo: String = "Electronico"

    override fun calcularImporte(): Double = precio * cantidad

    override fun etiqueta(): String = "$nombre [$tipo]"

    override fun descripcionGarantia(): String = "Garantia de $mesesGarantia meses"
}

class ProductoAlmacenamiento(
    nombre: String,
    precio: Double,
    cantidad: Int,
    val capacidadGB: Int
) : ProductoBase(nombre, precio, cantidad) {

    override val tipo: String = "Almacenamiento"

    override fun calcularImporte(): Double = precio * cantidad

    override fun etiqueta(): String = "$nombre [$tipo, ${capacidadGB}GB]"

    override fun descripcionGarantia(): String = "Cambio por defecto de fabrica"
}

class Carrito(val cliente: String) {

    private val productos = mutableListOf<ProductoBase>()

    val cantidadProductos: Int
        get() = productos.size

    fun agregar(producto: ProductoBase) {
        productos.add(producto)
    }

    fun buscar(nombre: String): ProductoBase? {
        return productos.find { it.nombre == nombre }
    }

    fun eliminar(nombre: String) {
        productos.removeIf { it.nombre == nombre }
    }

    fun calcularSubtotal(): Double {
        var subtotal = 0.0
        for (p in productos) {
            subtotal += p.calcularImporte()
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

    fun productoMasCaro(): ProductoBase? = productos.maxByOrNull { it.precio }

    fun listar(): List<ProductoBase> = productos
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
            println(String.format("%d. %-20s x%d  S/ %8.2f",
                i, p.nombre, p.cantidad, p.calcularImporte()))
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

    // Polimorfismo: el bucle no sabe ni pregunta de que tipo es cada
    // producto. Llama al mismo metodo y cada objeto responde a su modo.
    fun imprimirClasificacion() {
        println()
        println("------ CLASIFICACION DE PRODUCTOS ------")
        for (p in carrito.listar()) {
            println(String.format("%-32s %s", p.etiqueta(), p.descripcionGarantia()))
        }
        println("---------------------------------------")
    }

    fun imprimirDespedida() {
        println()
        println("Gracias por su compra, ${carrito.cliente}!")
    }
}

fun main() {
    val carrito = Carrito("Renzo Leon")
    val reporte = ReporteConsola(carrito)

    carrito.agregar(ProductoElectronico("Laptop HP", 2500.0, 1, 24))
    carrito.agregar(ProductoElectronico("Mouse Logitech", 45.5, 2, 12))
    carrito.agregar(ProductoElectronico("Audifonos Sony", 120.0, 1, 12))
    carrito.agregar(ProductoAlmacenamiento("USB Kingston 64GB", 25.0, 3, 64))

    reporte.imprimirCabecera()
    reporte.imprimirProductosAgregados()
    reporte.imprimirDetalle()
    reporte.imprimirTotales()
    println("---------------------------------------")
    reporte.imprimirProductoMasCaro()
    reporte.imprimirDescuento()

    reporte.imprimirClasificacion()

    // ---------- Reto adicional ----------

    println()
    println("--------- BUSCAR PRODUCTO ---------")
    val encontrado = carrito.buscar("Mouse Logitech")
    if (encontrado != null) {
        println("Producto encontrado: ${encontrado.etiqueta()}")
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
