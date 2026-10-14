package com.example.insertcode_grupojm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.insertcode_grupojm.navigation.AppNavigation
import com.example.insertcode_grupojm.ui.theme.InsertCode_GrupoJMTheme
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import com.example.insertcode_grupojm.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {

    // Una sola instancia del LoginViewModel para toda la app (login y Home comparten sesión)
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InsertCode_GrupoJMTheme {
                // El Scaffold vive dentro de cada pantalla; aquí solo se monta la navegación
                AppNavigation(loginViewModel = loginViewModel)
            }
        }
    }
}
