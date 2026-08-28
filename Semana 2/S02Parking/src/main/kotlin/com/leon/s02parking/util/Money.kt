package com.leon.s02parking.util

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Formatea un importe como moneda con exactamente 2 decimales, por ejemplo "S/ 4.50".
 */
fun BigDecimal.aMoneda(): String = "S/ " + this.setScale(2, RoundingMode.HALF_UP).toPlainString()
