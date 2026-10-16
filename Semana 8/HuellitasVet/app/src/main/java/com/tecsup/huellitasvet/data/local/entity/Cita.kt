package com.tecsup.huellitasvet.data.local.entity

// [A] TÚ LO HACES — Paso 1
// Contrato:
// - @Entity(tableName = "citas") con ForeignKey mascotaId -> Mascota y servicioId -> Servicio
// - Index en mascotaId, servicioId y en (fecha, hora)
// - id, mascotaId, servicioId, fecha ('aaaa-MM-dd'), hora ('HH:mm'), nota, estado (EstadoCita.name),
//   precio, diagnostico?, tratamiento?, pesoRegistrado?, fechaCreacion = System.currentTimeMillis()
