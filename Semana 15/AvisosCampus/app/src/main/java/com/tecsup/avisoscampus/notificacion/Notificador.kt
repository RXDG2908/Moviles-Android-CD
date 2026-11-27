package com.tecsup.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.tecsup.avisoscampus.model.Aviso

object Notificador {
    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT)
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(canal)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2
        return false
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3: comprobar permiso, construir y mostrar la notificación
        // TODO Paso 4: abrir la app al tocarla
        return false
    }
}
