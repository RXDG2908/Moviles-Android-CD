package com.leon.s02parking

import com.leon.s02parking.model.ParkingCalculator
import com.leon.s02parking.model.ParkingRecord
import com.leon.s02parking.model.VehicleType
import com.leon.s02parking.util.aMoneda
import kotlin.system.exitProcess

/**
 * Vehículo en el que se pregunta si desea seguir registrando. Solo aplica
 * si el aforo declarado por el usuario es mayor a este número.
 */
const val UMBRAL_AVISO = 3

/**
 * Cantidad máxima de horas que se puede registrar para un vehículo.
 */
const val HORAS_MAXIMAS = 48

/**
 * Cantidad máxima razonable de vehículos que se puede pedir registrar de
 * una sola vez, para evitar overflow de enteros y sesiones absurdas.
 */
const val CANTIDAD_MAXIMA_A_REGISTRAR = 10_000

private const val ANCHO_RESUMEN = 44

/**
 * Control de Estacionamiento — aplicación de consola.
 */
fun main() {
    println("=".repeat(40))
    println("     CONTROL DE ESTACIONAMIENTO")
    println("=".repeat(40))

    var registrados = 0
    val aforo = leerCantidadARegistrar("Aforo (capacidad máxima) de vehículos: ")
    var tope = leerCantidadARegistrar("¿Cuántos vehículos desea registrar? ", aforo)
    var avisoMostrado = false

    while (registrados < tope) {
        println()
        val placa = leerPlaca()
        val tipo = leerTipoVehiculo()
        val horas = leerHoras()
        val clienteFrecuente = leerClienteFrecuente()

        val registro = ParkingCalculator.calcular(placa, tipo, horas, clienteFrecuente)
        registrados++
        imprimirResumen(registro, registrados, tope)

        if (!avisoMostrado && registrados == UMBRAL_AVISO && tope > UMBRAL_AVISO) {
            avisoMostrado = true
            val seguir = preguntarSiNo("\n¿Desea seguir registrando vehículos? (s/n): ")
            if (!seguir) {
                break
            }
            val disponible = aforo - registrados
            val adicional = leerCantidadARegistrar("¿Cuántos vehículos más desea registrar? ", disponible)
            tope = registrados + adicional
        }
    }

    println()
    if (registrados >= tope) {
        println("Meta de registro alcanzada ($registrados/$tope). No se pueden registrar más vehículos.")
    }
    println("Gracias por usar el sistema de estacionamiento.")
}

/**
 * Lee una línea de la entrada estándar. Si la entrada se cerró (EOF, por
 * ejemplo al agotarse un archivo o pipe usado como entrada), termina el
 * programa de forma controlada en vez de quedar en un bucle infinito
 * repitiendo la pregunta.
 */
fun leerLinea(): String {
    val linea = readLine()
    if (linea == null) {
        println()
        println("No hay más datos de entrada. Cerrando el programa.")
        exitProcess(0)
    }
    return linea.trim()
}

/**
 * Lee cuántos vehículos se desean registrar. Debe ser un número entero
 * entre 1 y [maximo] (por defecto, el tope general [CANTIDAD_MAXIMA_A_REGISTRAR]).
 */
fun leerCantidadARegistrar(mensaje: String, maximo: Int = CANTIDAD_MAXIMA_A_REGISTRAR): Int {
    while (true) {
        print(mensaje)
        val entrada = leerLinea()
        if (entrada.isBlank()) {
            println("Ingresa la cantidad de vehículos a registrar.")
            continue
        }
        val cantidad = entrada.toIntOrNull()
        if (cantidad == null) {
            println("Ingresa la cantidad de vehículos a registrar.")
            continue
        }
        if (cantidad < 1) {
            println("La cantidad debe ser como mínimo 1.")
            continue
        }
        if (cantidad > maximo) {
            println("El aforo solo permite $maximo vehículos más.")
            continue
        }
        return cantidad
    }
}

/**
 * Lee la placa del vehículo por consola. Vuelve a preguntar si está vacía
 * o si excede la longitud razonable de una placa.
 */
fun leerPlaca(): String {
    while (true) {
        print("Placa: ")
        val entrada = leerLinea()
        if (entrada.isBlank()) {
            println("Ingresa la placa.")
            continue
        }
        if (entrada.length > 10) {
            println("La placa no puede tener más de 10 caracteres.")
            continue
        }
        return entrada.uppercase()
    }
}

/**
 * Muestra el menú de tipos de vehículo y lee la opción elegida.
 */
fun leerTipoVehiculo(): VehicleType {
    while (true) {
        println("Tipo de vehículo:")
        println("  1. Moto")
        println("  2. Auto")
        println("  3. Camioneta")
        println("  4. Trailer")
        print("Selecciona una opción: ")
        when (leerLinea()) {
            "1" -> return VehicleType.MOTO
            "2" -> return VehicleType.AUTO
            "3" -> return VehicleType.CAMIONETA
            "4" -> return VehicleType.TRAILER
            else -> println("Selecciona el tipo de vehículo.")
        }
    }
}

/**
 * Lee la cantidad de horas. Debe ser un número entero entre 1 y [HORAS_MAXIMAS].
 */
fun leerHoras(): Int {
    while (true) {
        print("Horas: ")
        val entrada = leerLinea()
        if (entrada.isBlank()) {
            println("Ingresa la cantidad de horas.")
            continue
        }
        val horas = entrada.toIntOrNull()
        if (horas == null) {
            println("Ingresa la cantidad de horas.")
            continue
        }
        if (horas < 1) {
            println("La cantidad de horas debe ser como mínimo 1.")
            continue
        }
        if (horas > HORAS_MAXIMAS) {
            println("La cantidad de horas no puede ser mayor a $HORAS_MAXIMAS.")
            continue
        }
        return horas
    }
}

/**
 * Pregunta si el cliente es frecuente (s/n).
 */
fun leerClienteFrecuente(): Boolean = preguntarSiNo("Cliente frecuente (s/n): ")

/**
 * Pregunta genérica de sí/no por consola, con reintento ante respuesta inválida.
 */
fun preguntarSiNo(mensaje: String): Boolean {
    while (true) {
        print(mensaje)
        when (leerLinea().lowercase()) {
            "s", "si", "sí" -> return true
            "n", "no" -> return false
            else -> println("Responde con 's' o 'n'.")
        }
    }
}

/**
 * Imprime el resumen completo del cobro: detalle hora por hora, subtotal,
 * descuento y total, con exactamente 2 decimales, además del contador de
 * vehículos registrados frente al tope de esta sesión.
 */
fun imprimirResumen(registro: ParkingRecord, registrados: Int, tope: Int) {
    println()
    println("=".repeat(ANCHO_RESUMEN))
    println("  RESUMEN DEL ESTACIONAMIENTO")
    println("=".repeat(ANCHO_RESUMEN))
    println("  Placa:             ${registro.placa}")
    println("  Tipo:              ${registro.tipo.etiqueta}")
    println("  Horas:             ${registro.horas}")
    println("  Cliente frecuente: ${if (registro.clienteFrecuente) "Sí" else "No"}")
    println("-".repeat(ANCHO_RESUMEN))
    println("  %-6s %-10s %-9s %s".format("HORA", "TARIFA", "RECARGO", "IMPORTE"))
    registro.detalle.forEach { hora ->
        println(
            "  %-6d %-10s %-9s %s".format(
                hora.hora,
                hora.tarifa.aMoneda(),
                "${hora.recargoPorcentaje}%",
                hora.importe.aMoneda()
            )
        )
    }
    println("-".repeat(ANCHO_RESUMEN))
    println("  %-24s%16s".format("Subtotal:", registro.subtotal.aMoneda()))
    println("  %-24s%16s".format("Descuento frecuente:", registro.descuentoFrecuente.aMoneda()))
    println("  %-24s%16s".format("Descuento por monto:", registro.descuentoPorMonto.aMoneda()))
    println("  %-24s%16s".format("IGV (18%):", registro.igv.aMoneda()))
    println("=".repeat(ANCHO_RESUMEN))
    println("  %-24s%16s".format("TOTAL:", registro.total.aMoneda()))
    println("=".repeat(ANCHO_RESUMEN))
    println("  Vehículos registrados: $registrados/$tope")
    println("=".repeat(ANCHO_RESUMEN))
}
