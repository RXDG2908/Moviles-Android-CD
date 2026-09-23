package com.example.semana05_navegacion.data

// Datos de un alumno del directorio
data class Alumno(
    val id: Int,
    val nombre: String,
    val carrera: String,
    val codigo: String,
    val correo: String,
    val facultad: String,
    val biografia: String
)

val alumnos = listOf(
    Alumno(1, "Juan León", "Ingeniería de Sistemas", "2024-0001", "juan.leon@example.com",
        "Ingeniería y Tecnología", "Estudiante destacado con interés en desarrollo Android."),
    Alumno(2, "Maria Garcia", "Arquitectura", "2024-0002", "maria.garcia@example.com",
        "Arquitectura y Diseño", "Apasionada por el diseño sostenible y el urbanismo."),
    Alumno(3, "Carlos Perez", "Medicina", "2024-0003", "carlos.perez@example.com",
        "Ciencias de la Salud", "Interesado en la investigación clínica y la salud pública."),
    Alumno(4, "Ana Lopez", "Derecho", "2024-0004", "ana.lopez@example.com",
        "Derecho y Ciencias Políticas", "Enfocada en derecho digital y protección de datos."),
    Alumno(5, "Luis Ramirez", "Administración", "2024-0005", "luis.ramirez@example.com",
        "Ciencias Empresariales", "Emprendedor con interés en gestión de proyectos.")
)
