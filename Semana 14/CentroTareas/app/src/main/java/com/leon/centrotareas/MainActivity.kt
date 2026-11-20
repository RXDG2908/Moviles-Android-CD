// Actividad principal: crea el ViewModel y muestra la pantalla de tareas.
package com.leon.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.leon.centrotareas.ui.PantallaTareas
import com.leon.centrotareas.ui.theme.CentroTareasTheme
import com.leon.centrotareas.viewmodel.TareasViewModel
import com.leon.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    private val vm: TareasViewModel by viewModels { TareasViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CentroTareasTheme {
                PantallaTareas(vm)
            }
        }
    }
}
