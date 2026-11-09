package com.tecsup.fortnite.ui.lista

import com.tecsup.fortnite.domain.model.Item
import java.text.Normalizer

// Quita tildes y pasa a minúsculas: "Cucharína" -> "cucharina"
fun normalizar(texto: String): String =
    Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
        .lowercase()
        .trim()

// true si todas las palabras buscadas aparecen en el nombre, la sección o el tipo
fun coincide(item: Item, busqueda: String): Boolean {
    val palabras = normalizar(busqueda).split(" ").filter { it.isNotBlank() }
    if (palabras.isEmpty()) return true
    val donde = normalizar("${item.titulo} ${item.seccion} ${item.subtitulo}")
    return palabras.all { donde.contains(it) }
}
