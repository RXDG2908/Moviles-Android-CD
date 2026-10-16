package com.tecsup.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.huellitasvet.data.local.dao.CitaDao
import com.tecsup.huellitasvet.data.local.dao.MascotaDao
import com.tecsup.huellitasvet.data.local.dao.ServicioDao
import com.tecsup.huellitasvet.data.local.dao.UsuarioDao
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario

@Database(
    entities = [Servicio::class, Usuario::class, Mascota::class, Cita::class],
    version = 2, // subió de 1 a 2 porque se agregaron 3 tablas al proyecto base
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {
    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao

    companion object {
        @Volatile private var INSTANCE: VetDatabase? = null

        // Singleton: una sola instancia de la base para toda la app
        fun obtener(context: Context): VetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                VetDatabase::class.java,
                "huellitasvet.db"
            )
                // Si cambia la versión, borra y vuelve a crear la base (datos de prueba)
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}
