package com.tecsup.fortnite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tecsup.fortnite.domain.model.Item
import com.tecsup.fortnite.ui.UiState
import com.tecsup.fortnite.ui.lista.ListaScreen
import com.tecsup.fortnite.ui.theme.TecsupNiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Datos de prueba mientras se conecta la API
        val prueba = listOf(
            Item("1", "Katana de Vader", "Pico", null, false, 800, 800, "Saga Star Wars", "starwars", "pickaxe"),
            Item("2", "Metal Gear Mk. II", "Mochila", null, true, 500, 700, "Épico", "epic", "backpack"),
            Item("3", "Jonesy", "Traje", null, false, 1500, 1500, "Poco común", "uncommon", "outfit")
        )
        setContent {
            TecsupNiteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    ListaScreen(
                        estado = UiState.Exito(prueba),
                        aviso = null,
                        onReintentar = {},
                        onItemClick = {},
                        onFavorito = {},
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
