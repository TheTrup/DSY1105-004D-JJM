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
import com.example.insertcode_grupojm.data.local.AppDatabase
import com.example.insertcode_grupojm.data.local.PreferencesManager
import com.example.insertcode_grupojm.navigation.AppNavigation
import com.example.insertcode_grupojm.navigation.NavigationViewModel
import com.example.insertcode_grupojm.repository.UsuarioRepository
import com.example.insertcode_grupojm.ui.theme.InsertCode_GrupoJMTheme
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import com.example.insertcode_grupojm.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {

    private val navigationViewModel: NavigationViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización de la base de datos Room y DataStore Preferences
        val database = AppDatabase.getDatabase(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)
        val usuarioRepository = UsuarioRepository(database.usuarioDao(), preferencesManager)

        setContent {
            InsertCode_GrupoJMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        navigationViewModel = navigationViewModel,
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }
}