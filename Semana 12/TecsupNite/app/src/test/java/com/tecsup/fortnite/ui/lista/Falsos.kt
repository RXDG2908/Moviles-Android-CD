package com.tecsup.fortnite.ui.lista

import com.tecsup.fortnite.data.repository.ItemRepository
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.notification.Notificador
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// Repositorio falso: guarda los items en memoria y deja elegir qué devuelve refrescar()
class RepositorioFalso(iniciales: List<Item> = emptyList()) : ItemRepository {

    private val guardados = MutableStateFlow(iniciales)

    // Lo que devolverá la próxima sincronización
    var resultadoRefrescar: Result<List<Item>> = Result.success(emptyList())
    var vecesRefrescar = 0

    override val items: Flow<List<Item>> = guardados
    override val favoritos: Flow<List<Item>> = guardados.map { lista -> lista.filter { it.esFavorito } }

    override suspend fun refrescar(): Result<Int> {
        vecesRefrescar++
        return resultadoRefrescar.map { nuevos ->
            guardados.value = nuevos
            nuevos.size
        }
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> =
        guardados.value.find { it.id == id }?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException(id))

    override suspend fun alternarFavorito(id: String) {
        guardados.value = guardados.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
    }

    override suspend fun cantidadGuardada() = guardados.value.size
    override suspend fun cantidadFavoritos() = guardados.value.count { it.esFavorito }
    override suspend fun limpiarCache() {
        guardados.value = emptyList()
    }
}

// Notificador falso: anota los avisos en vez de mostrarlos
class NotificadorFalso : Notificador {
    val avisos = mutableListOf<String>()
    override fun mostrar(titulo: String, texto: String) {
        avisos.add(texto)
    }
}

fun itemDePrueba(id: String, titulo: String = "Item $id", precio: Int = 500) =
    Item(id = id, titulo = titulo, subtitulo = "Pico", imagenUrl = null, precio = precio, precioRegular = precio)
