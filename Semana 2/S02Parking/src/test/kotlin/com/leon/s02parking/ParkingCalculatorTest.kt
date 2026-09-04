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
        assertEquals(BigDecimal("1.28"), registro.descuentoFrecuente)
        assertEquals(BigDecimal("0.00"), registro.descuentoPorMonto)
        assertEquals(BigDecimal("11.52"), registro.total)
    }

    @Test
    fun `caso 6 camioneta 6 horas`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.CAMIONETA, 6, false)
        assertEquals(BigDecimal("70.00"), registro.total)
    }

    @Test
    fun `recargo hora 5 sigue siendo 20 por ciento`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(VehicleType.AUTO.tarifaPorHora, 5)
        assertEquals(20, detalle[4].recargoPorcentaje)
    }

    @Test
    fun `recargo hora 6 pasa a 40 por ciento`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(VehicleType.AUTO.tarifaPorHora, 6)
        assertEquals(40, detalle[5].recargoPorcentaje)
    }

    @Test
    fun `recargo hora 10 sigue siendo 40 por ciento`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(VehicleType.AUTO.tarifaPorHora, 10)
        assertEquals(40, detalle[9].recargoPorcentaje)
    }

    @Test
    fun `recargo hora 11 pasa a 50 por ciento`() {
        val detalle = ParkingCalculator.calcularDetallePorHora(VehicleType.AUTO.tarifaPorHora, 11)
        assertEquals(50, detalle[10].recargoPorcentaje)
    }

    @Test
    fun `caso 7 trailer 1 hora`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.TRAILER, 1, false)
        assertEquals(BigDecimal("20.00"), registro.subtotal)
    }

    @Test
    fun `sin descuento por monto cuando el subtotal es exactamente 500`() {
        val descuento = ParkingCalculator.calcularDescuentoPorMonto(BigDecimal("500.00"), BigDecimal("500.00"))
        assertEquals(BigDecimal("0.00"), descuento)
    }

    @Test
    fun `descuento por monto del 20 por ciento cuando el subtotal supera 500`() {
        val descuento = ParkingCalculator.calcularDescuentoPorMonto(BigDecimal("600.00"), BigDecimal("600.00"))
        assertEquals(BigDecimal("120.00"), descuento)
    }

    @Test
    fun `caso 8 trailer 19 horas cliente frecuente combina ambos descuentos`() {
        val registro = ParkingCalculator.calcular("ABC-123", VehicleType.TRAILER, 19, true)
        assertEquals(BigDecimal("522.00"), registro.subtotal)
        assertEquals(BigDecimal("52.20"), registro.descuentoFrecuente)
        assertEquals(BigDecimal("93.96"), registro.descuentoPorMonto)
        assertEquals(BigDecimal("375.84"), registro.total)
    }
}
