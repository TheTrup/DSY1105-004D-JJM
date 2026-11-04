package com.example.insertcode_grupojm.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.insertcode_grupojm.ui.components.BottomNavBar
import com.example.insertcode_grupojm.ui.screens.dashboard.AlertasScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.HomeScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.PerfilScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.VentasScreen
import com.example.insertcode_grupojm.viewmodel.MainViewModel
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel,
    perfilViewModel: PerfilViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    LaunchedEffect(Unit) {
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpToRoute?.let { popUp ->
                            popUpTo(popUp.route) { inclusive = event.inclusive }
                        }
                        launchSingleTop = event.singleTop
                        restoreState = true
                    }
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

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
            composable(Screen.Perfil.route) { PerfilScreen(viewModel = perfilViewModel) }
        }
    }
}