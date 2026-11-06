package com.tecsup.authfirebaseappleon

import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth

private enum class Pantalla { LOGIN, REGISTRO, HOME }

@Composable
fun AuthApp() {
    val auth = remember { FirebaseAuth.getInstance() }
    var pantalla by remember {
        mutableStateOf(if (auth.currentUser != null) Pantalla.HOME else Pantalla.LOGIN)
    }

    when (pantalla) {
        Pantalla.LOGIN -> LoginScreen(
            auth = auth,
            onLoginOk = { pantalla = Pantalla.HOME },
            onIrRegistro = { pantalla = Pantalla.REGISTRO }
        )
        Pantalla.REGISTRO -> RegisterScreen(
            auth = auth,
            onRegistroOk = { pantalla = Pantalla.HOME },
            onVolver = { pantalla = Pantalla.LOGIN }
        )
        Pantalla.HOME -> HomeScreen(
            auth = auth,
            onLogout = {
                auth.signOut()
                pantalla = Pantalla.LOGIN
            }
        )
    }
}
