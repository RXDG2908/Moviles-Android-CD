package com.leon.s02parking.model

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Lógica de negocio para calcular el cobro de un estacionamiento.
 *
 * Reglas de recargo sobre la tarifa básica por hora:
 * - Horas 1 y 2: 0% de recargo.
 * - Horas 3 a 5: 20% de recargo.
 * - Hora 6 en adelante: 50% de recargo.
 *
 * Si el cliente es frecuente, se aplica un descuento del 10% sobre el subtotal.
 */
object ParkingCalculator {

    private val RECARGO_TRAMO_MEDIO = BigDecimal("0.20") // horas 3 a 5
    private val RECARGO_TRAMO_ALTO = BigDecimal("0.50") // hora 6 en adelante
    private val DESCUENTO_FRECUENTE = BigDecimal("0.10")
    private const val ESCALA = 2

    /**
     * Calcula el detalle de cobro hora por hora, aplicando el recargo que corresponda.
     */
    fun calcularDetallePorHora(tarifaBase: BigDecimal, horas: Int): List<HourDetail> {
        val tarifaRedondeada = tarifaBase.setScale(ESCALA, RoundingMode.HALF_UP)
        return (1..horas).map { hora ->
            val recargoPorcentaje = when {
                hora <= 2 -> 0
                hora <= 5 -> 20
                else -> 50
            }
            val recargo = when (recargoPorcentaje) {
                20 -> RECARGO_TRAMO_MEDIO
                50 -> RECARGO_TRAMO_ALTO
                else -> BigDecimal.ZERO
            }
            val importe = tarifaRedondeada.multiply(BigDecimal.ONE + recargo)
                .setScale(ESCALA, RoundingMode.HALF_UP)
            HourDetail(hora, tarifaRedondeada, recargoPorcentaje, importe)
        }
    }

    /**
     * Suma el importe de todas las horas del detalle.
     */
    fun calcularSubtotal(detalle: List<HourDetail>): BigDecimal =
        detalle.fold(BigDecimal.ZERO) { acumulado, hora -> acumulado + hora.importe }
            .setScale(ESCALA, RoundingMode.HALF_UP)

    /**
     * Descuento del 10% sobre el subtotal, solo para clientes frecuentes.
     */
    fun calcularDescuento(subtotal: BigDecimal, clienteFrecuente: Boolean): BigDecimal =
        if (clienteFrecuente) {
            subtotal.multiply(DESCUENTO_FRECUENTE).setScale(ESCALA, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(ESCALA, RoundingMode.HALF_UP)
        }

    /**
     * Total a pagar luego de restar el descuento al subtotal.
     */
    fun calcularTotal(subtotal: BigDecimal, descuento: BigDecimal): BigDecimal =
        subtotal.subtract(descuento).setScale(ESCALA, RoundingMode.HALF_UP)

    /**
     * Calcula el registro completo de cobro para un vehículo.
     */
    fun calcular(
        placa: String,
        tipo: VehicleType,
        horas: Int,
        clienteFrecuente: Boolean
    ): ParkingRecord {
        val detalle = calcularDetallePorHora(tipo.tarifaPorHora, horas)
        val subtotal = calcularSubtotal(detalle)
        val descuento = calcularDescuento(subtotal, clienteFrecuente)
        val total = calcularTotal(subtotal, descuento)
        return ParkingRecord(
            placa = placa,
            tipo = tipo,
            horas = horas,
            clienteFrecuente = clienteFrecuente,
            detalle = detalle,
            subtotal = subtotal,
            descuento = descuento,
            total = total
        )
    }
}
