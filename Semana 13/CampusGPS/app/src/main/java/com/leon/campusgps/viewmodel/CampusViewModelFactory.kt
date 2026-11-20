// Crea el MapaViewModel entregándole sus repositorios.
package com.leon.campusgps.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leon.campusgps.data.LugaresRepositoryLocal
import com.leon.campusgps.data.RutasRepositoryGoogle
import com.leon.campusgps.data.UbicacionRepositoryGoogle

class CampusViewModelFactory(context: Context) : ViewModelProvider.Factory {

    // applicationContext para no guardar la Activity
    private val appContext = context.applicationContext

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            return MapaViewModel(
                UbicacionRepositoryGoogle(appContext),
                LugaresRepositoryLocal(),
                RutasRepositoryGoogle(appContext)
            ) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
