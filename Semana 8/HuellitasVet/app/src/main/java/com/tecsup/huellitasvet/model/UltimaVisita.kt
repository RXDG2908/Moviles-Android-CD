package com.tecsup.huellitasvet.model

// Resultado de la consulta "última visita" (MAX(fecha) de las citas ATENDIDAS por mascota)
data class UltimaVisita(
    val mascotaId: Int,
    val fecha: String
)
