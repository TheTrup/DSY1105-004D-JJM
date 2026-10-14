package com.example.insertcode_grupojm.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.insertcode_grupojm.repository.AuthRepository
import com.example.insertcode_grupojm.repository.PreferenciasRepository

// Fábrica necesaria porque LoginViewModel recibe dependencias en su constructor.
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            authRepository = AuthRepository(),
            preferencias = PreferenciasRepository(context.applicationContext)
        ) as T
    }
}
