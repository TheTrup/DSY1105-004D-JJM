package com.example.insertcode_grupojm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.insertcode_grupojm.data.local.AppDatabase
import com.example.insertcode_grupojm.data.local.PreferencesManager
import com.example.insertcode_grupojm.navigation.AppNavigation
import com.example.insertcode_grupojm.repository.UsuarioRepository
import com.example.insertcode_grupojm.ui.theme.InsertCodeTheme
import com.example.insertcode_grupojm.viewmodel.MainViewModel
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicialización de Persistencia (Room + DataStore)
        val database = AppDatabase.getDatabase(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val usuarioRepository = UsuarioRepository(database.usuarioDao(), preferencesManager)

        // 2. Inyección manual de ViewModels compartidos
        val mainViewModel: MainViewModel by viewModels()
        val perfilViewModel: PerfilViewModel by viewModels {
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PerfilViewModel(usuarioRepository) as T
                }
            }
        }

        setContent {
            InsertCodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        mainViewModel = mainViewModel,
                        perfilViewModel = perfilViewModel
                    )
                }
            }
        }
    }
}