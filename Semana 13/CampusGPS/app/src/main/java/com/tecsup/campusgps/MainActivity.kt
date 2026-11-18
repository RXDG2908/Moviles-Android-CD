// Actividad principal: crea el ViewModel y muestra la pantalla del mapa.
package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.tecsup.campusgps.ui.PantallaMapa
import com.tecsup.campusgps.ui.theme.CampusGPSTheme
import com.tecsup.campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.campusgps.viewmodel.MapaViewModel

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
