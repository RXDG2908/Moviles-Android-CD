package com.leon.s02parking

import com.leon.s02parking.model.ParkingCalculator
import com.leon.s02parking.model.ParkingRecord
import com.leon.s02parking.model.VehicleType
import com.leon.s02parking.util.aMoneda

/**
 * Control de Estacionamiento — aplicación de consola.
 */
fun main() {
    println("=".repeat(40))
    println("     CONTROL DE ESTACIONAMIENTO")
    println("=".repeat(40))

    var continuar = true
    while (continuar) {
        println()
        val placa = leerPlaca()
        val tipo = leerTipoVehiculo()
        val horas = leerHoras()
        val clienteFrecuente = leerClienteFrecuente()

        val registro = ParkingCalculator.calcular(placa, tipo, horas, clienteFrecuente)
        imprimirResumen(registro)

        continuar = preguntarSiNo("\n¿Registrar otro vehículo? (s/n): ")
    }

    println()
    println("Gracias por usar el sistema de estacionamiento.")
}

/**
 * Lee la placa del vehículo por consola. Vuelve a preguntar si está vacía.
 */
fun leerPlaca(): String {
    while (true) {
        print("Placa: ")
        val entrada = readLine()?.trim().orEmpty()
        if (entrada.isBlank()) {
            println("Ingresa la placa.")
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
        print("Selecciona una opción: ")
        when (readLine()?.trim()) {
            "1" -> return VehicleType.MOTO
            "2" -> return VehicleType.AUTO
            "3" -> return VehicleType.CAMIONETA
            else -> println("Selecciona el tipo de vehículo.")
        }
    }
}

/**
 * Lee la cantidad de horas. Debe ser un número entero >= 1.
 */
fun leerHoras(): Int {
    while (true) {
        print("Horas: ")
        val entrada = readLine()?.trim().orEmpty()
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
        when (readLine()?.trim()?.lowercase()) {
            "s", "si", "sí" -> return true
            "n", "no" -> return false
            else -> println("Responde con 's' o 'n'.")
        }
    }
}

/**
 * Imprime el resumen completo del cobro: detalle hora por hora, subtotal,
 * descuento y total, con exactamente 2 decimales.
 */
fun imprimirResumen(registro: ParkingRecord) {
    println()
    println("-".repeat(40))
    println("RESUMEN DEL ESTACIONAMIENTO")
    println("-".repeat(40))
    println("Placa: ${registro.placa}")
    println("Tipo: ${registro.tipo.etiqueta}")
    println("Horas: ${registro.horas}")
    println("Cliente frecuente: ${if (registro.clienteFrecuente) "Sí" else "No"}")
    println()
    println("DETALLE:")
    println()
    registro.detalle.forEach { hora ->
        val recargo = "${hora.recargoPorcentaje}%"
        println(
            "Hora %-2d   %-10s %-6s %s".format(
                hora.hora,
                hora.tarifa.aMoneda(),
                recargo,
                hora.importe.aMoneda()
            )
        )
    }
    println()
    println("Subtotal:              ${registro.subtotal.aMoneda()}")
    println("Descuento frecuente:   ${registro.descuento.aMoneda()}")
    println("-".repeat(40))
    println("TOTAL:                 ${registro.total.aMoneda()}")
    println("-".repeat(40))
}
