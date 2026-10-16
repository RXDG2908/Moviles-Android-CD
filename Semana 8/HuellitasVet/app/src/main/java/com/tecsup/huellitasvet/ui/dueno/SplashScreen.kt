package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import kotlinx.coroutines.delay

// [E]
@Composable
fun SplashScreen(onTerminar: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1200)
        onTerminar()
    }
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MascotaAvatar(Especie.PERRO, tamano = 110.dp)
        Spacer(Modifier.height(16.dp))
        Text("HuellitasVet", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Text("Tu veterinaria de confianza", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(24.dp))
        CircularProgressIndicator()
    }
}
