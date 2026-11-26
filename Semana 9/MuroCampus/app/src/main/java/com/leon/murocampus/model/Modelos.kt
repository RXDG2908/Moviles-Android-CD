// Clases que representan el JSON del servicio; los nombres son iguales a los del JSON.
package com.leon.murocampus.model

import kotlinx.serialization.Serializable

// Ejemplo resuelto: un usuario del servicio
@Serializable
data class Usuario(
    val id: Int,
    val name: String,
    val email: String
)

// Una publicación: GET posts y GET posts/{id}
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

// Un comentario: GET posts/{id}/comments
@Serializable
data class Comentario(
    val postId: Int,
    val id: Int,
    val name: String,
    val email: String,
    val body: String
)

// Lo que enviamos en el POST; no lleva id porque lo pone el servidor
@Serializable
data class NuevoPost(
    val userId: Int,
    val title: String,
    val body: String
)
