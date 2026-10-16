package com.tecsup.huellitasvet.data.local.relation

// [A] TÚ LO HACES — Paso 2
// Contrato:
// - @Embedded val cita: Cita
// - @Relation(entity = Mascota::class, parentColumn = "mascotaId", entityColumn = "id") val mascota: MascotaConDueno
// - @Relation(parentColumn = "servicioId", entityColumn = "id") val servicio: Servicio
