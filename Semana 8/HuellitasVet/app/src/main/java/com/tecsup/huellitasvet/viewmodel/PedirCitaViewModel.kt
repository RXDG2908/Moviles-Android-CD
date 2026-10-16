package com.tecsup.huellitasvet.viewmodel

// [A] TÚ LO HACES — Paso 9
// Contrato:
// - Estados: mascota, servicio, fecha (hoy por defecto) y hora elegidos
// - horasOcupadas: StateFlow con flatMapLatest sobre observarHorasOcupadas(fecha)
// - Si la fecha cambia, limpia la hora. Si es hoy, solo horas futuras
// - confirmar(): vuelve a validar que el horario siga libre y llama a repo.crearCita
