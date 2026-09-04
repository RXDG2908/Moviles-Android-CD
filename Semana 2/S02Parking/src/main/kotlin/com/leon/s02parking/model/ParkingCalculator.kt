package com.leon.s02parking.model

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Lógica de negocio para calcular el cobro de un estacionamiento.
 *
 * Reglas de recargo sobre la tarifa básica por hora:
 * - Horas 1 y 2: 0% de recargo.
 * - Horas 3 a 5: 20% de recargo.
 * - Horas 6 a 10: 40% de recargo.
 * - Hora 11 en adelante: 50% de recargo.
 *
 * Si el cliente es frecuente, se aplica un descuento del 10% sobre el subtotal.
 * Si el subtotal (antes de descuentos) supera S/500, se aplica además un
 * descuento adicional del 20% sobre el monto ya restado el de cliente
 * frecuente.
 */
object ParkingCalculator {

    private val RECARGO_TRAMO_MEDIO = BigDecimal("0.20") // horas 3 a 5
    private val RECARGO_TRAMO_ALTO = BigDecimal("0.40") // horas 6 a 10
    private val RECARGO_TRAMO_MAXIMO = BigDecimal("0.50") // hora 11 en adelante
    private val DESCUENTO_FRECUENTE = BigDecimal("0.10")
    private val DESCUENTO_POR_MONTO = BigDecimal("0.20")
    private val UMBRAL_DESCUENTO_POR_MONTO = BigDecimal("500")
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
                hora <= 10 -> 40
                else -> 50
            }
            val recargo = when (recargoPorcentaje) {
                20 -> RECARGO_TRAMO_MEDIO
                40 -> RECARGO_TRAMO_ALTO
                50 -> RECARGO_TRAMO_MAXIMO
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
    fun calcularDescuentoFrecuente(subtotal: BigDecimal, clienteFrecuente: Boolean): BigDecimal =
        if (clienteFrecuente) {
            subtotal.multiply(DESCUENTO_FRECUENTE).setScale(ESCALA, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(ESCALA, RoundingMode.HALF_UP)
        }

    /**
     * Descuento adicional del 20% cuando el subtotal original (antes de
     * descuentos) supera S/500. Se calcula sobre el monto que ya quedó
     * luego de restar el descuento de cliente frecuente.
     */
    fun calcularDescuentoPorMonto(subtotalOriginal: BigDecimal, montoTrasFrecuente: BigDecimal): BigDecimal =
        if (subtotalOriginal > UMBRAL_DESCUENTO_POR_MONTO) {
            montoTrasFrecuente.multiply(DESCUENTO_POR_MONTO).setScale(ESCALA, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(ESCALA, RoundingMode.HALF_UP)
        }

    /**
     * Total a pagar luego de restar ambos descuentos al subtotal.
     */
    fun calcularTotal(subtotal: BigDecimal, descuentoFrecuente: BigDecimal, descuentoPorMonto: BigDecimal): BigDecimal =
        subtotal.subtract(descuentoFrecuente).subtract(descuentoPorMonto).setScale(ESCALA, RoundingMode.HALF_UP)

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
        val descuentoFrecuente = calcularDescuentoFrecuente(subtotal, clienteFrecuente)
        val montoTrasFrecuente = subtotal.subtract(descuentoFrecuente).setScale(ESCALA, RoundingMode.HALF_UP)
        val descuentoPorMonto = calcularDescuentoPorMonto(subtotal, montoTrasFrecuente)
        val total = calcularTotal(subtotal, descuentoFrecuente, descuentoPorMonto)
        return ParkingRecord(
            placa = placa,
            tipo = tipo,
            horas = horas,
            clienteFrecuente = clienteFrecuente,
            detalle = detalle,
            subtotal = subtotal,
            descuentoFrecuente = descuentoFrecuente,
            descuentoPorMonto = descuentoPorMonto,
            total = total
        )
    }
}
