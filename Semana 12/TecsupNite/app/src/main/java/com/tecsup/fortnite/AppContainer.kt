// Crea y guarda los objetos compartidos (API, base de datos, repositorios).
package com.tecsup.fortnite

import android.content.Context
import androidx.room.Room
import com.tecsup.fortnite.data.auth.AuthRepository
import com.tecsup.fortnite.data.auth.FirebaseAuthRepository
import com.tecsup.fortnite.data.local.AppDatabase
import com.tecsup.fortnite.data.remote.RetrofitClient
import com.tecsup.fortnite.data.repository.ItemRepository
import com.tecsup.fortnite.data.repository.ItemRepositoryImpl
import com.tecsup.fortnite.notification.Notificador
import com.tecsup.fortnite.notification.NotificadorAndroid

// Crea la API, la base de datos y los repositorios una sola vez
class AppContainer(context: Context) {

    private val baseDeDatos = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "tecsupnite.db"
    )
        // Al cambiar la tabla se vuelve a crear; los datos se recuperan al sincronizar
        .fallbackToDestructiveMigration()
        .build()

    val itemRepository: ItemRepository = ItemRepositoryImpl(RetrofitClient.api, baseDeDatos.itemDao())
    val authRepository: AuthRepository = FirebaseAuthRepository()
    val notificador: Notificador = NotificadorAndroid(context.applicationContext)
}
