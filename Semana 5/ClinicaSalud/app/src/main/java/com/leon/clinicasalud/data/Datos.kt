package com.leon.clinicasalud.data

// Una cita agendada por el paciente
data class Cita(
    val medico: String,
    val fecha: String,
    val hora: String,
    val estado: String            // "Confirmada" o "Completada"
)

val medicos: List<MedicoBase> = listOf(
    MedicoGeneral(1, "Dra. Ana Torres", "Cardiología", 4.9, 128, "12 años exp.",
        "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."),
    MedicoGeneral(2, "Dr. Luis Vega", "Pediatría", 4.7, 96, "8 años exp.",
        "Atención de niños y adolescentes, control de crecimiento y vacunas."),
    MedicoGeneral(3, "Dra. Rosa Díaz", "Dermatología", 4.8, 74, "10 años exp.",
        "Tratamiento de enfermedades de la piel y dermatología estética.")
)

// Opciones de fecha y hora para agendar
val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
val horas = listOf("9:00 am", "10:30 am", "3:00 pm")

val historial = listOf(
    "12/08 — Control general — Dr. Luis Vega",
    "03/07 — Análisis de sangre — Laboratorio",
    "15/05 — Consulta de piel — Dra. Rosa Díaz"
)
