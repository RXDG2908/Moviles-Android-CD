package com.leon.s02parking.model

import java.math.BigDecimal

/**
 * Tipos de vehículo admitidos por el estacionamiento, con su tarifa básica por hora.
 */
enum class VehicleType(val etiqueta: String, val tarifaPorHora: BigDecimal) {
    MOTO("Moto", BigDecimal("2.00")),
    AUTO("Auto", BigDecimal("4.00")),
    CAMIONETA("Camioneta", BigDecimal("10.00"))
}
