package com.tecsup.huellitasvet

import android.app.Application
import com.tecsup.huellitasvet.data.local.DatosIniciales
import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Crea la base y el repository una sola vez para toda la app
class VetApp : Application() {

    val database: VetDatabase by lazy { VetDatabase.obtener(this) }
    val repository: VetRepository by lazy { VetRepositoryRoom(database) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Siembra los datos de prueba en segundo plano (solo la primera vez)
        appScope.launch { DatosIniciales.sembrar(database) }
    }
}
