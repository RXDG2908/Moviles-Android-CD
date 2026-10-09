package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.R
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrincipal
import com.saludplus.citas.ui.theme.AzulMarino
import com.saludplus.citas.ui.theme.AzulSalud
import com.saludplus.citas.ui.theme.Blanco
import com.saludplus.citas.ui.theme.TextoGris

// Pantalla 1 - Splash: logo, nombre de la clínica, lema, imagen y botones.
@Composable
fun SplashScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        // Logo de la clínica
        Image(
            painter = painterResource(R.drawable.logo_saludplus),
            contentDescription = "Logo Clínica SaludPlus",
            modifier = Modifier.size(width = 84.dp, height = 70.dp)
        )
        Spacer(Modifier.height(8.dp))

        // Nombre y lema
        Text(
            "Clínica\nSaludPlus",
            color = AzulMarino,
            fontSize = 30.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text("Tu salud, nuestra prioridad", color = TextoGris, fontSize = 14.sp)

        // Imagen del doctor (ocupa el espacio del medio)
        Image(
            painter = painterResource(R.drawable.doctor_splash),
            contentDescription = "Doctor",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp)
        )

        // Botones
        BotonPrincipal("Comenzar") { navController.navigate(Rutas.REGISTRO) }
        TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
            Text("Ya tengo una cuenta", color = AzulSalud, fontWeight = FontWeight.SemiBold)
        }
    }
}
