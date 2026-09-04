package com.leon.s02parking.model

import java.math.BigDecimal

/**
 * Detalle del cobro correspondiente a una hora individual de estacionamiento.
 */
data class HourDetail(
    val hora: Int,
    val tarifa: BigDecimal,
    val recargoPorcentaje: Int,
    val importe: BigDecimal
)

/**
 * Registro de un cobro de estacionamiento ya calculado.
 */
data class ParkingRecord(
    val placa: String,
    val tipo: VehicleType,
    val horas: Int,
    val clienteFrecuente: Boolean,
    val detalle: List<HourDetail>,
    val subtotal: BigDecimal,
    val descuentoFrecuente: BigDecimal,
    val descuentoPorMonto: BigDecimal,
    val igv: BigDecimal,
    val total: BigDecimal
)
