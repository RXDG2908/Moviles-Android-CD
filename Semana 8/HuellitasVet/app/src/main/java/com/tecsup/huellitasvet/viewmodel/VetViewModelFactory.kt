package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.huellitasvet.data.repository.VetRepository

// [E] Crea los ViewModels pasándoles el Repository (interface)
class VetViewModelFactory(private val repo: VetRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ServiciosViewModel::class.java) -> ServiciosViewModel(repo)
        // TODO: registra aquí cada ViewModel nuevo (Sesion, Inicio, Mascota, PedirCita, ...)
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    } as T
}
