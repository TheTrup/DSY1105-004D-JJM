package com.example.insertcode_grupojm.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.insertcode_grupojm.ui.components.BottomNavBar
import com.example.insertcode_grupojm.ui.screens.dashboard.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Inicio.route,
        Screen.Ventas.route,
        Screen.Alertas.route,
        Screen.Perfil.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Inicio.route) { HomeScreen() }
            composable(Screen.Ventas.route) { VentasScreen() }
            composable(Screen.Alertas.route) { AlertasScreen() }
            composable(Screen.Perfil.route) { PerfilScreen() }
        }
    }
}