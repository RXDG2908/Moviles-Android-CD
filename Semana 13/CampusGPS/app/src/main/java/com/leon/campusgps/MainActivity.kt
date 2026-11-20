// Actividad principal: crea el ViewModel y muestra la pantalla del mapa.
package com.leon.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.leon.campusgps.ui.PantallaMapa
import com.leon.campusgps.ui.theme.CampusGPSTheme
import com.leon.campusgps.viewmodel.CampusViewModelFactory
import com.leon.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    private val vm: MapaViewModel by viewModels { CampusViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CampusGPSTheme {
                PantallaMapa(vm)
            }
        }
    }
}
