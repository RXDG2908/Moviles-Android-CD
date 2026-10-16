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

// [G] Crea la base y el repository una sola vez para toda la app
class VetApp : Application() {

    val database: VetDatabase by lazy { VetDatabase.obtener(this) }
    val repository: VetRepository by lazy { VetRepositoryRoom(database) }
    // TODO (Paso 5): val preferencias by lazy { PreferenciasStore(this) }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        appScope.launch { DatosIniciales.sembrar(database) }
    }
}
