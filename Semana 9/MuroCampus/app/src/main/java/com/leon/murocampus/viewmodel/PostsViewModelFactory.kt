// Crea el PostsViewModel y le pasa el Repository que usa Retrofit.
package com.leon.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.leon.murocampus.data.PostRepositoryRemote
import com.leon.murocampus.data.remote.RetrofitClient

class PostsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PostsViewModel(PostRepositoryRemote(RetrofitClient.api)) as T
}
