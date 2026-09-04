package com.leon.s02parking

import com.leon.s02parking.model.ParkingCalculator
import com.leon.s02parking.model.VehicleType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ParkingCalculatorTest {

    @Test
    fun `caso 1 moto 1 hora`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.MOTO, 1, false)
        assertEquals(BigDecimal("2.00"), registro.total)
    }

    @Test
    fun `caso 2 auto 2 horas`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.AUTO, 2, false)
        assertEquals(BigDecimal("8.00"), registro.total)
    }

    @Test
    fun `caso 3 ejemplo del pizarron con tarifa de 4 soles`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(BigDecimal("4.00"), 3)
        val subtotal = ParkingCalculator.calcularSubtotal(detalle)
        assertEquals(BigDecimal("12.80"), subtotal)
    }

    @Test
    fun `caso 4 auto 3 horas`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.AUTO, 3, false)
        assertEquals(BigDecimal("12.80"), registro.total)
    }

    @Test
    fun `caso 5 auto 3 horas cliente frecuente`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.AUTO, 3, true)
        assertEquals(BigDecimal("12.80"), registro.subtotal)
        assertEquals(BigDecimal("1.28"), registro.descuento)
        assertEquals(BigDecimal("11.52"), registro.total)
    }

    @Test
    fun `caso 6 camioneta 6 horas`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.CAMIONETA, 6, false)
        assertEquals(BigDecimal("71.00"), registro.total)
    }

    @Test
    fun `recargo hora 5 sigue siendo 20 por ciento`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(VehicleType.AUTO.tarifaPorHora, 5)
        assertEquals(20, detalle[4].recargoPorcentaje)
    }

    @Test
    fun `recargo hora 6 pasa a 50 por ciento`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(VehicleType.AUTO.tarifaPorHora, 6)
        assertEquals(50, detalle[5].recargoPorcentaje)
    }

    @Test
    fun `caso 7 trailer 1 hora`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.TRAILER, 1, false)
        assertEquals(BigDecimal("20.00"), registro.subtotal)
    }
}
