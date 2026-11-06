package com.tecsup.authfirebaseappleon

// Modelo de un curso guardado en Cloud Firestore (colección "cursos").
// uid = id del usuario dueño del curso (sirve para que cada usuario vea solo los suyos).
data class Curso(
    val id: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val creditos: Int = 0,
    val uid: String = ""
)
