package com.tecsup.huellitasvet.model

// Resultado de la consulta "citas por servicio" (JOIN + GROUP BY) del panel.
// Los nombres de las propiedades deben coincidir con los alias del SELECT.
data class ConteoServicio(
    val nombre: String,
    val cantidad: Int
)
