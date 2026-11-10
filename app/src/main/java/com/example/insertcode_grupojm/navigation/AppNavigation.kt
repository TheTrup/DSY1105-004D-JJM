package com.example.insertcode_grupojm.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.insertcode_grupojm.ui.components.BottomNavBar
import com.example.insertcode_grupojm.ui.screen.LoginScreen
import com.example.insertcode_grupojm.ui.screen.RegisterScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.AlertasScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.HomeScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.PerfilScreen
import com.example.insertcode_grupojm.ui.screens.dashboard.VentasScreen
import com.example.insertcode_grupojm.ui.utils.rememberTamanoVentana
import com.example.insertcode_grupojm.viewmodel.LoginViewModel
import com.example.insertcode_grupojm.viewmodel.PerfilViewModel
import com.example.insertcode_grupojm.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppNavigation(
    navigationViewModel: NavigationViewModel,
    loginViewModel: LoginViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Escucha eventos de navegación emitidos desde los ViewModels
    LaunchedEffect(Unit) {
        navigationViewModel.eventos.collectLatest { evento ->
            when (evento) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(evento.route) {
                        evento.popUpTo?.let { target ->
                            popUpTo(target) { inclusive = evento.inclusive }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                is NavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    // La barra inferior de navegación solo se muestra dentro del Dashboard
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
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- Flujo de Autenticación ---
            composable(Screen.Login.route) {
                val loginUiState = loginViewModel.estado.collectAsState().value
                LoginScreen(
                    estado = loginUiState,
                    tamano = rememberTamanoVentana(),
                    onEmailChange = loginViewModel::onEmailChange,
                    onPasswordChange = loginViewModel::onPasswordChange,
                    onSimularSinConexionChange = loginViewModel::onSimularSinConexionChange,
                    onLoginClick = {
                        loginViewModel.onLoginClick { _ ->
                            navigationViewModel.navigateTo(
                                route = Screen.Inicio.route,
                                popUpTo = Screen.Login.route,
                                inclusive = true
                            )
                        }
                    },
                    onOAuthSuccess = {
                        navigationViewModel.navigateTo(
                            route = Screen.Inicio.route,
                            popUpTo = Screen.Login.route,
                            inclusive = true
                        )
                    },
                    onRegistroClick = {
                        navigationViewModel.navigateTo(Screen.Register.route)
                    }
                )
            }

            composable(Screen.Register.route) {
                val registerViewModel: RegisterViewModel = viewModel()
                RegisterScreen(
                    onNavigateBack = { navigationViewModel.navigateBack() },
                    onRegisterSuccess = {
                        navigationViewModel.navigateTo(
                            route = Screen.Inicio.route,
                            popUpTo = Screen.Login.route,
                            inclusive = true
                        )
                    },
                    viewModel = registerViewModel
                )
            }

            // --- Flujo del Dashboard Principal ---
            composable(Screen.Inicio.route) {
                HomeScreen()
            }

            composable(Screen.Ventas.route) {
                VentasScreen()
            }

            composable(Screen.Alertas.route) {
                AlertasScreen()
            }

            composable(Screen.Perfil.route) {
                val perfilViewModel: PerfilViewModel = viewModel()
                PerfilScreen(viewModel = perfilViewModel)
            }
        }
    }
}