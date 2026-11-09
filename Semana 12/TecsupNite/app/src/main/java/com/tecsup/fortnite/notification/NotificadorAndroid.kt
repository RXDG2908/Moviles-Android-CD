package com.tecsup.fortnite.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.fortnite.MainActivity
import com.tecsup.fortnite.R

class NotificadorAndroid(private val context: Context) : Notificador {

    private val idCanal = "sincronizacion"

    init {
        // El canal se crea una sola vez (desde Android 8)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                idCanal,
                "Sincronización",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    override fun mostrar(titulo: String, texto: String) {
        // Si el usuario negó el permiso, la app sigue normal pero sin avisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        // Al tocar la notificación se abre la app
        val intent = Intent(context, MainActivity::class.java)
        val pendiente = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificacion = NotificationCompat.Builder(context, idCanal)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setContentIntent(pendiente)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1, notificacion)
    }
}
