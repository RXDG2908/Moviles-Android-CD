package com.leon.tecsupfit.data

// Una clase del gimnasio que se muestra en la lista de Inicio
data class Clase(
    val id: Int,
    val nombre: String,
    val dia: String,          // "Hoy" o "Esta semana" (sirve para el filtro)
    val horario: String,
    val sala: String,
    val duracion: String,
    val descripcion: String,
    val cupos: String,
    val horarios: List<String> // opciones para elegir antes de reservar
)

// Una reserva hecha por el usuario
data class Reserva(
    val clase: String,
    val horario: String,
    val estado: String        // "Confirmada" o "Completada"
)

val clases = listOf(
    Clase(1, "Yoga funcional", "Hoy", "7:00 am", "Sala 2", "60 min",
        "Clase de estiramiento y respiración para empezar el día.",
        "5 de 15 cupos disponibles", listOf("7:00 am", "9:00 am", "6:00 pm")),
    Clase(2, "Cross Training", "Hoy", "6:00 pm", "Sala 1", "45 min",
        "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        "8 de 12 cupos disponibles", listOf("6:00 pm", "7:00 pm", "8:00 pm")),
    Clase(3, "Spinning", "Hoy", "7:30 pm", "Sala 3", "50 min",
        "Ciclismo en bicicleta estática al ritmo de la música.",
        "10 de 20 cupos disponibles", listOf("7:30 am", "12:30 pm", "7:30 pm")),
    Clase(4, "Pilates", "Esta semana", "Jue 8:00 am", "Sala 2", "55 min",
        "Ejercicios de control y fuerza del abdomen.",
        "6 de 10 cupos disponibles", listOf("8:00 am", "10:00 am", "5:00 pm")),
    Clase(5, "Box", "Esta semana", "Vie 7:00 pm", "Sala 1", "60 min",
        "Técnica de golpes y resistencia.",
        "4 de 12 cupos disponibles", listOf("5:00 pm", "6:00 pm", "7:00 pm"))
)

val rutinas = listOf(
    "Rutina de piernas — 4 ejercicios",
    "Rutina de brazos — 5 ejercicios",
    "Rutina de abdomen — 6 ejercicios",
    "Rutina de cardio — 20 minutos"
)
