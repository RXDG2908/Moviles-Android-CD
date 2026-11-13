// Clase Application: crea el contenedor al abrir la app.
package com.tecsup.fortnite

import android.app.Application

class TecsupNiteApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
