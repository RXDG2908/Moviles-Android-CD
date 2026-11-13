// Crea los ViewModels con sus dependencias.
package com.tecsup.fortnite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.fortnite.AppContainer
import com.tecsup.fortnite.ui.admin.AdminViewModel
import com.tecsup.fortnite.ui.detalle.DetalleViewModel
import com.tecsup.fortnite.ui.favoritos.FavoritosViewModel
import com.tecsup.fortnite.ui.lista.ListaViewModel
import com.tecsup.fortnite.ui.login.LoginViewModel

// Crea los ViewModel pasándoles lo que necesitan desde el AppContainer
class ViewModelFactory(
    private val container: AppContainer,
    private val id: String = ""
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(container.authRepository)
            ListaViewModel::class.java -> ListaViewModel(container.itemRepository, container.notificador)
            DetalleViewModel::class.java -> DetalleViewModel(container.itemRepository, id)
            FavoritosViewModel::class.java -> FavoritosViewModel(container.itemRepository)
            AdminViewModel::class.java -> AdminViewModel(container.itemRepository, container.notificador)
            else -> throw IllegalArgumentException("ViewModel desconocido")
        } as T
    }
}
