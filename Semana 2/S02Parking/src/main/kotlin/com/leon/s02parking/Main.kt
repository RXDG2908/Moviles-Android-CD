package com.leon.s02parking

import com.leon.s02parking.model.VehicleType

/**
 * Control de Estacionamiento — aplicación de consola.
 */
fun main() {
    println("=".repeat(40))
    println("     CONTROL DE ESTACIONAMIENTO")
    println("=".repeat(40))
    println()

    val placa = leerPlaca()
    val tipo = leerTipoVehiculo()
    val horas = leerHoras()
    val clienteFrecuente = leerClienteFrecuente()

    println()
    println("Datos registrados:")
    println("Placa: $placa")
    println("Tipo: ${tipo.etiqueta}")
    println("Horas: $horas")
    println("Cliente frecuente: ${if (clienteFrecuente) "Sí" else "No"}")
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
